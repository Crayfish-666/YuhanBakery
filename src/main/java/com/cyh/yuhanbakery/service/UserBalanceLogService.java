package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.entity.UserBalanceLog;

public interface UserBalanceLogService extends IService<UserBalanceLog> {
    IPage<UserBalanceLog> listLogsByUserId(Long userId, Integer pageNum, Integer pageSize);
}
