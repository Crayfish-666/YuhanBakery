package com.cyh.yuhanbakery.controller;

import com.cyh.yuhanbakery.common.Result;
import com.cyh.yuhanbakery.entity.Coupon;
import com.cyh.yuhanbakery.service.CouponService;
import com.cyh.yuhanbakery.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/coupon")
@RequiredArgsConstructor
public class CustomerCouponController {

    private final CouponService couponService;
    private final JwtUtils jwtUtils;

    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            Claims claims = jwtUtils.parseToken(token.substring(7));
            if (claims != null && !jwtUtils.isTokenExpired(claims)) {
                return jwtUtils.getUserId(claims);
            }
        }
        return null;
    }

    @PostMapping("/claim/{couponId}")
    public Result<?> claimCoupon(@RequestHeader(value = "Authorization", required = false) String token,
                                 @PathVariable("couponId") Long couponId) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) return Result.failed("请先登录");
        couponService.issueCoupon(userId, couponId);
        return Result.success(null, "领取成功");
    }

    @GetMapping("/my")
    public Result<List<Coupon>> getMyCoupons(@RequestHeader(value = "Authorization", required = false) String token) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) return Result.failed("请先登录");
        return Result.success(couponService.getAvailableCoupons(userId));
    }
}
