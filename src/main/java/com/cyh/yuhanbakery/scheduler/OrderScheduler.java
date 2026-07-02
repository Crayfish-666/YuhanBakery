package com.cyh.yuhanbakery.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyh.yuhanbakery.entity.Orders;
import com.cyh.yuhanbakery.mapper.OrdersMapper;
import com.cyh.yuhanbakery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrdersMapper ordersMapper;
    private final OrderService orderService;

    /**
     * 每隔 5 分钟扫描一次数据库，双重兜底释放 15 分钟前仍未支付的订单
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void scanTimeoutOrders() {
        log.info("【定时任务】开始扫描超时未支付的订单，准备进行库存补偿兜底...");
        // 计算 15 分钟前的时间节点
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(15);

        List<Orders> timeoutOrders = ordersMapper.selectList(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getStatus, 0) // 0-未支付
                .le(Orders::getCreateTime, thresholdTime));

        if (timeoutOrders.isEmpty()) {
            return;
        }

        log.info("发现 {} 个超时未支付订单，执行批量取消补偿...", timeoutOrders.size());
        for (Orders order : timeoutOrders) {
            try {
                orderService.cancelOrder(order.getOrderNo());
                log.info("定时任务成功兜底取消订单：{}", order.getOrderNo());
            } catch (Exception e) {
                log.error("定时任务取消订单发生错误：orderNo={}", order.getOrderNo(), e);
            }
        }
    }
}
