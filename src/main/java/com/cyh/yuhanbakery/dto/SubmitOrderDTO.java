package com.cyh.yuhanbakery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitOrderDTO {
    @NotNull(message = "配送类型不能为空")
    private Integer deliveryType; // 1-外卖配送，2-自提

    @NotBlank(message = "联系人电话不能为空")
    private String contactPhone;

    private String addressSnapshot; // 地址快照 (自提可为空，配送必须提供)

    private String remark; // 订单备注

    private Long couponId; // 用户选择使用的优惠券ID (可选)
}
