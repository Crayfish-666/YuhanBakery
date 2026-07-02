package com.cyh.yuhanbakery.service;

import java.util.Map;

public interface CartService {
    void addCart(Long userId, Long skuId, Integer quantity);
    Map<Long, Integer> listCart(Long userId);
    void updateQuantity(Long userId, Long skuId, Integer quantity);
    void deleteCart(Long userId, Long skuId);
    void clearCart(Long userId);
}
