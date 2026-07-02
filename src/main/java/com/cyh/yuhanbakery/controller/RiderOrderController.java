package com.cyh.yuhanbakery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyh.yuhanbakery.common.Result;
import com.cyh.yuhanbakery.entity.Orders;
import com.cyh.yuhanbakery.security.CustomUserDetails;
import com.cyh.yuhanbakery.service.RiderOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider/order")
@RequiredArgsConstructor
public class RiderOrderController {

    private final RiderOrderService riderOrderService;

    private Long getCurrentRiderId() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    @GetMapping("/available")
    public Result<IPage<Orders>> listAvailableOrders(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<Orders> list = riderOrderService.listAvailableOrders(pageNum, pageSize);
        return Result.success(list);
    }

    @PutMapping("/accept")
    public Result<?> acceptOrder(@RequestParam("orderId") Long orderId) {
        riderOrderService.acceptOrder(getCurrentRiderId(), orderId);
        return Result.success(null, "抢单成功！请尽快开启配送");
    }

    @GetMapping("/my")
    public Result<IPage<Orders>> listMyOrders(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        Page<Orders> page = new Page<>(pageNum, pageSize);
        // 查询当前骑手接单待取餐 (1) 配送中 (2) 或已送达 (3) 的订单记录
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getRiderId, getCurrentRiderId())
                .in(Orders::getStatus, 1, 2, 3)
                .orderByDesc(Orders::getUpdateTime);
        
        riderOrderService.page(page, queryWrapper);
        return Result.success(page);
    }

    @PutMapping("/complete")
    public Result<?> completeDelivery(@RequestParam("orderId") Long orderId) {
        riderOrderService.completeDelivery(orderId);
        return Result.success(null, "配送完成");
    }

    @PutMapping("/pickup")
    public Result<?> pickupOrder(@RequestParam("orderId") Long orderId) {
        riderOrderService.pickupOrder(orderId);
        return Result.success(null, "取餐成功，开始配送");
    }
}
