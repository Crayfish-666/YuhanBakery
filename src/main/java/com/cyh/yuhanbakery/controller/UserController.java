package com.cyh.yuhanbakery.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyh.yuhanbakery.common.Result;
import com.cyh.yuhanbakery.dto.LoginDTO;
import com.cyh.yuhanbakery.dto.RegisterDTO;
import com.cyh.yuhanbakery.entity.User;
import com.cyh.yuhanbakery.entity.UserAddress;
import com.cyh.yuhanbakery.entity.UserBalanceLog;
import com.cyh.yuhanbakery.security.CustomUserDetails;
import com.cyh.yuhanbakery.service.UserAddressService;
import com.cyh.yuhanbakery.service.UserBalanceLogService;
import com.cyh.yuhanbakery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserAddressService addressService;
    private final UserBalanceLogService balanceLogService;

    private Long getCurrentUserId() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        return Result.success(token, "登录成功");
    }

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success(null, "注册成功");
    }

    @GetMapping("/info")
    public Result<User> getUserInfo() {
        User user = userService.getById(getCurrentUserId());
        // 安全起见隐藏密码哈希
        if (user != null) {
            user.setPassword("******");
        }
        return Result.success(user);
    }

    @PostMapping("/topup")
    public Result<?> topup(@RequestParam("amount") BigDecimal amount) {
        userService.topup(getCurrentUserId(), amount);
        return Result.success(null, "充值成功");
    }

    @PostMapping("/withdraw")
    public Result<?> withdraw(@RequestParam("amount") BigDecimal amount) {
        userService.withdraw(getCurrentUserId(), amount);
        return Result.success(null, "提现成功，金额已到账");
    }

    @GetMapping("/address")
    public Result<List<UserAddress>> listAddresses() {
        List<UserAddress> list = addressService.listAddressesByUserId(getCurrentUserId());
        return Result.success(list);
    }

    @PostMapping("/address")
    public Result<?> saveAddress(@Valid @RequestBody UserAddress address) {
        addressService.saveAddress(getCurrentUserId(), address);
        return Result.success(null, "保存收货地址成功");
    }

    @PutMapping("/address/default/{addressId}")
    public Result<?> setDefaultAddress(@PathVariable("addressId") Long addressId) {
        addressService.setDefaultAddress(getCurrentUserId(), addressId);
        return Result.success(null, "设置默认收货地址成功");
    }

    @DeleteMapping("/address/{addressId}")
    public Result<?> deleteAddress(@PathVariable("addressId") Long addressId) {
        addressService.removeById(addressId);
        return Result.success(null, "删除地址成功");
    }

    @GetMapping("/balance-log")
    public Result<IPage<UserBalanceLog>> listBalanceLogs(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<UserBalanceLog> list = balanceLogService.listLogsByUserId(getCurrentUserId(), pageNum, pageSize);
        return Result.success(list);
    }
}
