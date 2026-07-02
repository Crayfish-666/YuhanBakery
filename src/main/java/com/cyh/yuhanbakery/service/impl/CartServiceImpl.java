package com.cyh.yuhanbakery.service.impl;

import com.cyh.yuhanbakery.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final StringRedisTemplate redisTemplate;

    private String getCartKey(Long userId) {
        return "bakery:cart:" + userId;
    }

    @Override
    public void addCart(Long userId, Long skuId, Integer quantity) {
        String key = getCartKey(userId);
        redisTemplate.opsForHash().increment(key, String.valueOf(skuId), quantity);
        log.info("添加购物车成功，userId={}, skuId={}, quantity={}", userId, skuId, quantity);
    }

    @Override
    public Map<Long, Integer> listCart(Long userId) {
        String key = getCartKey(userId);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<Long, Integer> result = new HashMap<>();
        entries.forEach((k, v) -> result.put(Long.parseLong(k.toString()), Integer.parseInt(v.toString())));
        return result;
    }

    @Override
    public void updateQuantity(Long userId, Long skuId, Integer quantity) {
        String key = getCartKey(userId);
        if (quantity == null || quantity <= 0) {
            deleteCart(userId, skuId);
        } else {
            redisTemplate.opsForHash().put(key, String.valueOf(skuId), String.valueOf(quantity));
            log.info("修改购物车商品数量，userId={}, skuId={}, quantity={}", userId, skuId, quantity);
        }
    }

    @Override
    public void deleteCart(Long userId, Long skuId) {
        String key = getCartKey(userId);
        redisTemplate.opsForHash().delete(key, String.valueOf(skuId));
        log.info("删除购物车商品，userId={}, skuId={}", userId, skuId);
    }

    @Override
    public void clearCart(Long userId) {
        String key = getCartKey(userId);
        redisTemplate.delete(key);
        log.info("清空购物车，userId={}", userId);
    }
}
