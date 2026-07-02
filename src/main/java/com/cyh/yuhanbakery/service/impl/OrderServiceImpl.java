package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.common.BusinessException;
import com.cyh.yuhanbakery.dto.SubmitOrderDTO;
import com.cyh.yuhanbakery.entity.*;
import com.cyh.yuhanbakery.mapper.*;
import com.cyh.yuhanbakery.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cyh.yuhanbakery.config.RabbitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrderService {

    private final StringRedisTemplate redisTemplate;
    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserMapper userMapper;
    private final UserBalanceLogMapper balanceLogMapper;
    private final ProductSkuMapper productSkuMapper;
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    private final RabbitTemplate rabbitTemplate;

    private DefaultRedisScript<Long> deductStockScript;

    @PostConstruct
    public void init() {
        deductStockScript = new DefaultRedisScript<>();
        deductStockScript.setResultType(Long.class);
        deductStockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/deduct_stock.lua")));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitOrder(Long userId, SubmitOrderDTO dto) {
        // 1. 读取该用户 Redis 购物车列表
        String cartKey = "bakery:cart:" + userId;
        Map<Object, Object> cartItems = redisTemplate.opsForHash().entries(cartKey);
        if (cartItems.isEmpty()) {
            throw new BusinessException("您的购物车是空的，快去选购吧！");
        }

        // 解析 SKU 列表与数量
        Map<Long, Integer> skuQtyMap = new HashMap<>();
        cartItems.forEach((k, v) -> skuQtyMap.put(Long.parseLong(k.toString()), Integer.parseInt(v.toString())));

        // 2. 数据库读取规格最新价格，防止前端造假或价格变更
        List<ProductSku> skus = productSkuMapper.selectBatchIds(skuQtyMap.keySet());
        if (skus.size() != skuQtyMap.size()) {
            throw new BusinessException("购物车中部分商品已失效");
        }
        Map<Long, ProductSku> skuMap = skus.stream().collect(Collectors.toMap(ProductSku::getId, s -> s));

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : skuQtyMap.entrySet()) {
            ProductSku sku = skuMap.get(entry.getKey());
            if (sku == null || sku.getStatus() == 0 || sku.getIsDeleted() == 1) {
                throw new BusinessException("商品规格 [" + (sku != null ? sku.getSpecName() : "") + "] 已下架");
            }
            BigDecimal itemPrice = sku.getPrice().multiply(new BigDecimal(entry.getValue()));
            totalAmount = totalAmount.add(itemPrice);
        }

        // --- 优惠券逻辑处理 ---
        UserCoupon userCoupon = null;
        if (dto.getCouponId() != null) {
            List<UserCoupon> userCoupons = userCouponMapper.selectList(new LambdaQueryWrapper<UserCoupon>()
                    .eq(UserCoupon::getUserId, userId)
                    .eq(UserCoupon::getCouponId, dto.getCouponId())
                    .eq(UserCoupon::getStatus, 0)
                    .last("LIMIT 1"));
            if (userCoupons == null || userCoupons.isEmpty()) {
                throw new BusinessException("优惠券无效或已使用");
            }
            userCoupon = userCoupons.get(0);
            Coupon coupon = couponMapper.selectById(userCoupon.getCouponId());
            if (coupon == null || coupon.getStatus() == 0 || coupon.getIsDeleted() == 1) {
                throw new BusinessException("该优惠券已失效");
            }
            if (coupon.getEndTime() != null && coupon.getEndTime().isBefore(LocalDateTime.now())) {
                throw new BusinessException("优惠券已过期");
            }
            if (coupon.getMinAmount() != null && totalAmount.compareTo(coupon.getMinAmount()) < 0) {
                throw new BusinessException("订单金额未达到优惠券使用门槛");
            }

            // 计算折扣
            BigDecimal originalAmount = totalAmount;
            if (coupon.getType() == 1 || coupon.getType() == 3) { // 满减 或 无门槛
                totalAmount = totalAmount.subtract(coupon.getDiscountAmount());
            } else if (coupon.getType() == 2) { // 折扣
                totalAmount = totalAmount.multiply(coupon.getDiscountRate());
            }
            // 防止金额为负
            if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
                totalAmount = BigDecimal.ZERO;
            }
        }
        // -------------------

        // 3. 执行 Redis Lua 脚本预扣减缓存库存 (原子操作，防并发超卖)
        List<String> keys = new ArrayList<>();
        List<String> args = new ArrayList<>();
        skuQtyMap.forEach((skuId, qty) -> {
            keys.add("bakery:sku:stock:" + skuId);
            args.add(String.valueOf(qty));
        });

        Long result = redisTemplate.execute(deductStockScript, keys, args.toArray());
        if (result == null || result == -2) {
            throw new BusinessException("系统繁忙，库存未热加载成功");
        }
        if (result == -1) {
            throw new BusinessException("商品库存不足，下单失败！");
        }

        // 4. 在数据库中写入 Orders 订单主表
        String orderNo = "ORD" + System.currentTimeMillis() + String.format("%04d", new Random().nextInt(10000));
        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0); // 0-未支付
        order.setDeliveryType(dto.getDeliveryType());
        order.setContactPhone(dto.getContactPhone());
        order.setAddressSnapshot(dto.getAddressSnapshot());
        order.setVersion(1);
        order.setRemark(dto.getRemark());
        ordersMapper.insert(order);

        // 如果使用了优惠券，则更新使用状态
        if (dto.getCouponId() != null && userCoupon != null) {
            UserCoupon ucUpdate = new UserCoupon();
            ucUpdate.setId(userCoupon.getId());
            ucUpdate.setStatus(1); // 1-已使用
            ucUpdate.setUsedTime(LocalDateTime.now());
            ucUpdate.setOrderNo(orderNo);
            userCouponMapper.updateById(ucUpdate);
        }

        // 5. 写入 OrderItem 订单明细表
        for (Map.Entry<Long, Integer> entry : skuQtyMap.entrySet()) {
            ProductSku sku = skuMap.get(entry.getKey());
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(sku.getProductId());
            item.setSkuId(sku.getId());
            item.setComboId(null); // 如果有套餐可特殊处理，此处默认为单品关联
            item.setComboDetails(null);
            item.setPrice(sku.getPrice());
            item.setQuantity(entry.getValue());
            orderItemMapper.insert(item);
        }

        // 6. 异步清空 Redis 购物车
        redisTemplate.delete(cartKey);

        // 7. 投递延时消息到 RabbitMQ，发送至 TTL 队列以实现 15 分钟死信延迟 (无需插件支持)
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.TTL_QUEUE, orderNo);
            log.info("下单成功并发送至 TTL 队列：orderNo={}", orderNo);
        } catch (Exception e) {
            log.error("投递延时队列失败！", e);
            // 此时不应该直接阻断下单，可以依赖 Spring Schedule 定时器扫表进行最终兜底
        }

        return orderNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payWithWallet(Long userId, String orderNo) {
        // 1. 获取订单详情
        Orders order = ordersMapper.selectOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getOrderNo, orderNo));
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("当前订单状态不可支付");
        }

        // 2. 检查并原子扣减用户余额
        int rows = userMapper.deductBalance(userId, order.getTotalAmount());
        if (rows == 0) {
            throw new BusinessException("余额扣除失败，可能账户余额不足");
        }

        // 3. 记录扣款资金流水
        UserBalanceLog balanceLog = new UserBalanceLog();
        balanceLog.setUserId(userId);
        balanceLog.setAmount(order.getTotalAmount().negate());
        balanceLog.setType(2); // 2-消费
        balanceLog.setOrderNo(orderNo);
        balanceLogMapper.insert(balanceLog);

        // 4. 调用支付成功后的状态机流转与 DB 物理库存扣减
        this.paySuccess(orderNo, 1); // 1-余额支付
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(String orderNo, Integer payMethod) {
        // 1. 获取订单记录
        Orders order = ordersMapper.selectOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getOrderNo, orderNo));
        if (order == null) {
            log.warn("支付回调处理失败：订单 {} 不存在", orderNo);
            return;
        }

        // 2. 使用乐观锁防止并发重复回调
        if (order.getStatus() != 0) {
            log.info("订单 {} 已经是支付/已处理状态，忽略本次回调", orderNo);
            return;
        }

        // 3. 更新订单状态为“已支付，待接单 (status=1)”
        order.setStatus(1);
        order.setPayMethod(payMethod);
        order.setPayTime(LocalDateTime.now());
        
        int rows = ordersMapper.update(order, new LambdaQueryWrapper<Orders>()
                .eq(Orders::getId, order.getId())
                .eq(Orders::getStatus, 0)); // 乐观防并发拦截
        if (rows == 0) {
            log.info("并发回调，已被其它线程更新成功，忽略本次处理");
            return;
        }

        // 4. 真实扣减 MySQL 中的 SKU 物理库存
        List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, order.getId()));
        
        for (OrderItem item : items) {
            int deductRows = productSkuMapper.deductStock(item.getSkuId(), item.getQuantity());
            if (deductRows == 0) {
                // 极端情况下若发现 MySQL 库存不足（例如由于手动改库），需要在此进行抛出异常促使事务回滚
                // 并且通知运维报警，因为 Redis 和 DB 库存产生了偏离。
                throw new BusinessException("商品数据库库存扣减失败，库存不足");
            }
            log.info("支付成功，MySQL 物理库存扣减成功：skuId={}, qty={}", item.getSkuId(), item.getQuantity());
        }

        // 5. 增加用户积分 (1元 = 1积分，向上取整)
        int earnedPoints = order.getTotalAmount().setScale(0, java.math.RoundingMode.UP).intValue();
        if (earnedPoints > 0) {
            User user = userMapper.selectById(order.getUserId());
            if (user != null) {
                user.setPoints((user.getPoints() != null ? user.getPoints() : 0) + earnedPoints);
                userMapper.updateById(user);
                log.info("用户获得积分：userId={}, points={}", user.getId(), earnedPoints);
            }
        }

        log.info("订单支付流程处理完毕，已置为待接单状态：orderNo={}", orderNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderNo) {
        Orders order = ordersMapper.selectOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getOrderNo, orderNo));
        if (order == null) {
            return;
        }

        // 如果订单仍是未支付（status = 0），执行超时取消
        if (order.getStatus() == 0) {
            order.setStatus(4); // 4-已取消
            int rows = ordersMapper.update(order, new LambdaQueryWrapper<Orders>()
                    .eq(Orders::getId, order.getId())
                    .eq(Orders::getStatus, 0)); // 乐观拦截
            if (rows == 0) {
                return;
            }

            // 恢复 Redis 中的 SKU 库存
            List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                    .eq(OrderItem::getOrderId, order.getId()));
            for (OrderItem item : items) {
                redisTemplate.opsForValue().increment("bakery:sku:stock:" + item.getSkuId(), item.getQuantity());
                log.info("订单超时取消，已加回 Redis 中的 SKU 库存：skuId={}, quantity={}", item.getSkuId(), item.getQuantity());
            }
            log.info("订单超时未支付，已成功自动取消：orderNo={}", orderNo);
        }
    }
}
