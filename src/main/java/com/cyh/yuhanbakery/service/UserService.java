package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.dto.LoginDTO;
import com.cyh.yuhanbakery.dto.RegisterDTO;
import com.cyh.yuhanbakery.entity.User;

import java.math.BigDecimal;

public interface UserService extends IService<User> {
    String login(LoginDTO dto);
    void register(RegisterDTO dto);
    void topup(Long userId, BigDecimal amount);
    void withdraw(Long userId, BigDecimal amount);
}
