package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.entity.Coupon;

import java.util.List;

public interface CouponService extends IService<Coupon> {
    void issueCoupon(Long userId, Long couponId);
    List<Coupon> getAvailableCoupons(Long userId);
}
