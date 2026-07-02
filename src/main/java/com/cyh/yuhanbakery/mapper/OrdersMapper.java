package com.cyh.yuhanbakery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyh.yuhanbakery.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
    
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as date, SUM(total_amount) as sales " +
            "FROM orders WHERE status IN (1,2,3) AND create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') ORDER BY date ASC")
    List<Map<String, Object>> getWeeklySales();

    @Select("SELECT c.name as name, SUM(oi.quantity) as value " +
            "FROM order_item oi " +
            "JOIN product p ON oi.product_id = p.id " +
            "JOIN category c ON p.category_id = c.id " +
            "GROUP BY c.id, c.name")
    List<Map<String, Object>> getCategorySales();
}
