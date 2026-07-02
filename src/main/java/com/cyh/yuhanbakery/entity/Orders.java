package com.cyh.yuhanbakery.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer status; // 0-未支付，1-已支付(待接单)，2-配送中，3-已送达，4-已取消
    private Integer deliveryType; // 1-骑手配送，2-自提
    private Long riderId;
    private String addressSnapshot; // 地址快照
    private String contactPhone;
    private LocalDateTime appointmentTime;
    private LocalDateTime payTime;
    private Integer payMethod; // 1-余额，2-微信

    @Version
    private Integer version; // 乐观锁版本号

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
