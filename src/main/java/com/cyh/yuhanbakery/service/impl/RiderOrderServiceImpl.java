package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.common.BusinessException;
import com.cyh.yuhanbakery.entity.Orders;
import com.cyh.yuhanbakery.mapper.OrdersMapper;
import com.cyh.yuhanbakery.service.RiderOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderOrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements RiderOrderService {

    private final OrdersMapper ordersMapper;

    @Override
    public IPage<Orders> listAvailableOrders(Integer pageNum, Integer pageSize) {
        Page<Orders> page = new Page<>(pageNum, pageSize);
        // 展示所有：已支付/待抢单 (status=1) 且是 外卖配送 (deliveryType=1) 的订单，且无人接单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getStatus, 1)
                .eq(Orders::getDeliveryType, 1)
                .isNull(Orders::getRiderId)
                .orderByDesc(Orders::getCreateTime);
        return ordersMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptOrder(Long riderId, Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("该订单不存在");
        }
        if (order.getStatus() != 1 || order.getRiderId() != null) {
            throw new BusinessException("该订单不可抢占，可能已被抢走或已取消");
        }

        // 绑定骑手，但状态保持 1(待取餐)
        order.setRiderId(riderId);

        int rows = ordersMapper.updateById(order);
        if (rows == 0) {
            log.warn("骑手并发抢单冲突！riderId={}, orderId={}", riderId, orderId);
            throw new BusinessException("手慢了！该订单已被其它骑手抢走");
        }
        log.info("骑手抢单成功，riderId={}, orderId={}", riderId, orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pickupOrder(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("该订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("该订单状态错误，无法取餐");
        }

        order.setStatus(2); // 2-配送中
        ordersMapper.updateById(order);
        log.info("骑手确认取餐成功，orderId={}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeDelivery(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException("订单不处于配送中状态，无法操作完成");
        }

        order.setStatus(3); // 3-已送达/已完成
        ordersMapper.updateById(order);
        log.info("订单完成送达，orderId={}", orderId);
    }
}
