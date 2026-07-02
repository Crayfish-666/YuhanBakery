package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.dto.SubmitOrderDTO;
import com.cyh.yuhanbakery.entity.Orders;

public interface OrderService extends IService<Orders> {
    String submitOrder(Long userId, SubmitOrderDTO dto);
    void payWithWallet(Long userId, String orderNo);
    void paySuccess(String orderNo, Integer payMethod);
    void cancelOrder(String orderNo);
}
