package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.entity.UserAddress;

import java.util.List;

public interface UserAddressService extends IService<UserAddress> {
    List<UserAddress> listAddressesByUserId(Long userId);
    void saveAddress(Long userId, UserAddress address);
    void setDefaultAddress(Long userId, Long addressId);
}
