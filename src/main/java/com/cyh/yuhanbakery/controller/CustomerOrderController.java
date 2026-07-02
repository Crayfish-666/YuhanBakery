package com.cyh.yuhanbakery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyh.yuhanbakery.common.Result;
import com.cyh.yuhanbakery.dto.OrderVO;
import com.cyh.yuhanbakery.dto.SubmitOrderDTO;
import com.cyh.yuhanbakery.entity.OrderItem;
import com.cyh.yuhanbakery.entity.Orders;
import com.cyh.yuhanbakery.entity.Product;
import com.cyh.yuhanbakery.entity.ProductSku;
import com.cyh.yuhanbakery.mapper.OrderItemMapper;
import com.cyh.yuhanbakery.mapper.ProductMapper;
import com.cyh.yuhanbakery.mapper.ProductSkuMapper;
import com.cyh.yuhanbakery.security.CustomUserDetails;
import com.cyh.yuhanbakery.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/customer/order")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final OrderService orderService;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;

    private Long getCurrentUserId() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    @PostMapping("/submit")
    public Result<String> submitOrder(@Valid @RequestBody SubmitOrderDTO dto) {
        String orderNo = orderService.submitOrder(getCurrentUserId(), dto);
        return Result.success(orderNo, "下单成功，请在15分钟内完成支付");
    }

    @PostMapping("/pay")
    public Result<?> payWithWallet(@RequestParam("orderNo") String orderNo) {
        orderService.payWithWallet(getCurrentUserId(), orderNo);
        return Result.success(null, "支付成功");
    }

    /**
     * 微信支付模拟沙箱接口：免除微信证书，本地直接模拟回调处理
     */
    @PostMapping("/mock-wechat-pay")
    public Result<?> mockWeChatPay(@RequestParam("orderNo") String orderNo) {
        orderService.paySuccess(orderNo, 2); // 2-代表微信支付
        return Result.success(null, "模拟微信支付成功");
    }

    /**
     * 手动取消未支付订单
     */
    @PutMapping("/cancel")
    public Result<?> cancelOrder(@RequestParam("orderNo") String orderNo) {
        // 限额只能取消自己的未支付订单，后端 cancelOrder 已做状态校验
        orderService.cancelOrder(orderNo);
        return Result.success(null, "订单已成功取消，库存已归还");
    }

    @GetMapping("/list")
    public Result<IPage<OrderVO>> listOrders(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        Long userId = getCurrentUserId();
        Page<Orders> page = new Page<>(pageNum, pageSize);
        orderService.page(page, new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, userId)
                .orderByDesc(Orders::getCreateTime));

        Page<OrderVO> voPage = new Page<>(pageNum, pageSize);
        voPage.setTotal(page.getTotal());

        if (page.getRecords().isEmpty()) {
            voPage.setRecords(Collections.emptyList());
            return Result.success(voPage);
        }

        List<OrderVO> voList = new ArrayList<>();
        for (Orders order : page.getRecords()) {
            voList.add(assembleOrderVO(order));
        }

        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable("id") Long id) {
        Orders order = orderService.getById(id);
        if (order == null || !order.getUserId().equals(getCurrentUserId())) {
            return Result.failed("订单不存在或您无权查看此订单");
        }
        return Result.success(assembleOrderVO(order));
    }

    @GetMapping("/detailByNo/{orderNo}")
    public Result<OrderVO> getOrderDetailByNo(@PathVariable("orderNo") String orderNo) {
        Orders order = orderService.getOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getOrderNo, orderNo));
        if (order == null || !order.getUserId().equals(getCurrentUserId())) {
            return Result.failed("订单不存在或您无权查看此订单");
        }
        return Result.success(assembleOrderVO(order));
    }

    private OrderVO assembleOrderVO(Orders order) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        vo.setDeliveryType(order.getDeliveryType());
        vo.setRiderId(order.getRiderId());
        vo.setAddressSnapshot(order.getAddressSnapshot());
        vo.setContactPhone(order.getContactPhone());
        vo.setAppointmentTime(order.getAppointmentTime());
        vo.setPayTime(order.getPayTime());
        vo.setPayMethod(order.getPayMethod());
        vo.setRemark(order.getRemark());
        vo.setCreateTime(order.getCreateTime());

        // 获取明细及商品属性
        List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, order.getId()));
        
        if (!items.isEmpty()) {
            List<Long> productIds = items.stream().map(OrderItem::getProductId).distinct().collect(Collectors.toList());
            List<Product> products = productMapper.selectBatchIds(productIds);
            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));

            List<Long> skuIds = items.stream().map(OrderItem::getSkuId).distinct().collect(Collectors.toList());
            List<ProductSku> skus = productSkuMapper.selectBatchIds(skuIds);
            Map<Long, ProductSku> skuMap = skus.stream().collect(Collectors.toMap(ProductSku::getId, s -> s));

            List<OrderVO.OrderItemVO> itemVOList = items.stream().map(item -> {
                Product product = productMap.get(item.getProductId());
                ProductSku sku = skuMap.get(item.getSkuId());

                OrderVO.OrderItemVO iVo = new OrderVO.OrderItemVO();
                iVo.setId(item.getId());
                iVo.setProductId(item.getProductId());
                iVo.setSkuId(item.getSkuId());
                iVo.setSpecName(sku != null ? sku.getSpecName() : "");
                iVo.setProductName(product != null ? product.getName() : "");
                iVo.setProductImage(product != null ? product.getImage() : "");
                iVo.setPrice(item.getPrice());
                iVo.setQuantity(item.getQuantity());
                return iVo;
            }).collect(Collectors.toList());

            vo.setItems(itemVOList);
        } else {
            vo.setItems(Collections.emptyList());
        }

        return vo;
    }
}
