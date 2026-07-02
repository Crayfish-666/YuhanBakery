package com.cyh.yuhanbakery.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("combo")
public class Combo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private Integer status; // 0-下架，1-上架

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
