package com.cyh.yuhanbakery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyh.yuhanbakery.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("UPDATE user SET balance = balance - #{amount} WHERE id = #{userId} AND balance >= #{amount} AND is_deleted = 0")
    int deductBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);

    @Update("UPDATE user SET balance = balance + #{amount}, total_recharge = total_recharge + #{amount} WHERE id = #{userId} AND is_deleted = 0")
    int addBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}
