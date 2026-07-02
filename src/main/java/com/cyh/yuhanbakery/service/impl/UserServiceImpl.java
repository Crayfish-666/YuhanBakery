package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.common.BusinessException;
import com.cyh.yuhanbakery.common.ResultCode;
import com.cyh.yuhanbakery.dto.LoginDTO;
import com.cyh.yuhanbakery.dto.RegisterDTO;
import com.cyh.yuhanbakery.entity.User;
import com.cyh.yuhanbakery.entity.UserBalanceLog;
import com.cyh.yuhanbakery.mapper.UserBalanceLogMapper;
import com.cyh.yuhanbakery.mapper.UserMapper;
import com.cyh.yuhanbakery.service.UserService;
import com.cyh.yuhanbakery.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserBalanceLogMapper balanceLogMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public String login(LoginDTO dto) {
        // 根据用户名检索用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 验证密码是否匹配
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 生成 JWT Token 并返回
        return jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        // 校验用户名是否唯一
        Long countUsername = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (countUsername > 0) {
            throw new BusinessException("用户名已被注册");
        }

        // 校验手机号是否唯一
        Long countPhone = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, dto.getPhone()));
        if (countPhone > 0) {
            throw new BusinessException("手机号已被使用");
        }

        // 封装新用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setBalance(BigDecimal.ZERO);
        user.setTotalRecharge(BigDecimal.ZERO);

        userMapper.insert(user);
        log.info("新用户注册成功，ID={}, 角色={}", user.getId(), user.getRole());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void topup(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于零");
        }

        // 1. 原子累加余额和总充值额
        int rows = userMapper.addBalance(userId, amount);
        if (rows == 0) {
            throw new BusinessException("充值失败，用户不存在");
        }

        // 2. 写入资金审计日志流水
        UserBalanceLog logEntity = new UserBalanceLog();
        logEntity.setUserId(userId);
        logEntity.setAmount(amount);
        logEntity.setType(1); // 1-充值
        logEntity.setOrderNo(null);

        balanceLogMapper.insert(logEntity);
        log.info("用户充值成功，userId={}, amount={}", userId, amount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdraw(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("提现金额必须大于零");
        }

        // 1. 扣减余额
        int rows = userMapper.deductBalance(userId, amount);
        if (rows == 0) {
            throw new BusinessException("提现失败，余额不足或用户不存在");
        }

        // 2. 写入资金审计日志流水
        UserBalanceLog logEntity = new UserBalanceLog();
        logEntity.setUserId(userId);
        logEntity.setAmount(amount.negate()); // 提现记为负数
        logEntity.setType(4); // 4-提现
        logEntity.setOrderNo(null);

        balanceLogMapper.insert(logEntity);
        log.info("用户提现成功，userId={}, amount={}", userId, amount);
    }
}
