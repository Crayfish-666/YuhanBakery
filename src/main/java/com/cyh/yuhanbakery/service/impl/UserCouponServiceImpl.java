package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.entity.UserCoupon;
import com.cyh.yuhanbakery.mapper.UserCouponMapper;
import com.cyh.yuhanbakery.service.UserCouponService;
import org.springframework.stereotype.Service;

@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {
}
