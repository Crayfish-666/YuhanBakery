package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.entity.Orders;

public interface RiderOrderService extends IService<Orders> {
    IPage<Orders> listAvailableOrders(Integer pageNum, Integer pageSize);
    void acceptOrder(Long riderId, Long orderId);
    void pickupOrder(Long orderId);
    void completeDelivery(Long orderId);
}
