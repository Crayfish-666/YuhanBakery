package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.common.BusinessException;
import com.cyh.yuhanbakery.entity.UserAddress;
import com.cyh.yuhanbakery.mapper.UserAddressMapper;
import com.cyh.yuhanbakery.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    private final UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> listAddressesByUserId(Long userId) {
        return userAddressMapper.selectList(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .orderByDesc(UserAddress::getIsDefault)
                .orderByDesc(UserAddress::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAddress(Long userId, UserAddress address) {
        address.setUserId(userId);
        
        // 如果新增/编辑且是默认地址，需将现有的其它地址置为非默认
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefaultStatus(userId);
        }

        if (address.getId() == null) {
            userAddressMapper.insert(address);
        } else {
            userAddressMapper.updateById(address);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在或无权操作");
        }

        // 1. 清空所有的默认状态
        clearDefaultStatus(userId);

        // 2. 将当前设为默认
        address.setIsDefault(1);
        userAddressMapper.updateById(address);
        log.info("修改默认收货地址成功，userId={}, addressId={}", userId, addressId);
    }

    private void clearDefaultStatus(Long userId) {
        UserAddress temp = new UserAddress();
        temp.setIsDefault(0);
        userAddressMapper.update(temp, new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getIsDefault, 1));
    }
}
