package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.entity.UserBalanceLog;
import com.cyh.yuhanbakery.mapper.UserBalanceLogMapper;
import com.cyh.yuhanbakery.service.UserBalanceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBalanceLogServiceImpl extends ServiceImpl<UserBalanceLogMapper, UserBalanceLog> implements UserBalanceLogService {

    private final UserBalanceLogMapper balanceLogMapper;

    @Override
    public IPage<UserBalanceLog> listLogsByUserId(Long userId, Integer pageNum, Integer pageSize) {
        Page<UserBalanceLog> page = new Page<>(pageNum, pageSize);
        return balanceLogMapper.selectPage(page, new LambdaQueryWrapper<UserBalanceLog>()
                .eq(UserBalanceLog::getUserId, userId)
                .orderByDesc(UserBalanceLog::getCreateTime));
    }
}
