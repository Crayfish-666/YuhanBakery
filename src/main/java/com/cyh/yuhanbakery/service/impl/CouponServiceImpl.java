package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.common.BusinessException;
import com.cyh.yuhanbakery.entity.Coupon;
import com.cyh.yuhanbakery.entity.UserCoupon;
import com.cyh.yuhanbakery.mapper.CouponMapper;
import com.cyh.yuhanbakery.mapper.UserCouponMapper;
import com.cyh.yuhanbakery.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    private final UserCouponMapper userCouponMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issueCoupon(Long userId, Long couponId) {
        Coupon coupon = this.getById(couponId);
        if (coupon == null || coupon.getStatus() == 0 || coupon.getIsDeleted() == 1) {
            throw new BusinessException("优惠券不存在或已下架");
        }
        if (coupon.getStock() <= 0) {
            throw new BusinessException("优惠券已被领完");
        }
        
        // 判断有效期
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getEndTime() != null && coupon.getEndTime().isBefore(now)) {
            throw new BusinessException("优惠券已过期，无法领取");
        }

        // 简单限制每人领1张同一优惠券（可根据业务调整）
        Long count = userCouponMapper.selectCount(new LambdaQueryWrapper<UserCoupon>()
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getCouponId, couponId));
        if (count > 0) {
            throw new BusinessException("您已经领取过该优惠券了");
        }

        // 扣减库存
        coupon.setStock(coupon.getStock() - 1);
        this.updateById(coupon);

        // 增加用户领券记录
        UserCoupon uc = new UserCoupon();
        uc.setUserId(userId);
        uc.setCouponId(couponId);
        uc.setStatus(0); // 0-未使用
        userCouponMapper.insert(uc);
    }

    @Override
    public List<Coupon> getAvailableCoupons(Long userId) {
        // 查找用户所有未使用的优惠券
        List<UserCoupon> userCoupons = userCouponMapper.selectList(new LambdaQueryWrapper<UserCoupon>()
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getStatus, 0));

        if (userCoupons.isEmpty()) return List.of();

        List<Long> couponIds = userCoupons.stream().map(UserCoupon::getCouponId).collect(Collectors.toList());
        List<Coupon> coupons = this.listByIds(couponIds);

        // 过滤掉已过期的优惠券
        LocalDateTime now = LocalDateTime.now();
        return coupons.stream().filter(c -> c.getEndTime() == null || !c.getEndTime().isBefore(now)).collect(Collectors.toList());
    }
}
