package com.cyh.yuhanbakery.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private BigDecimal totalAmount;
    private Integer status; // 0-未支付，1-待接单，2-配送中，3-已送达，4-已取消
    private Integer deliveryType; // 1-外卖，2-自提
    private Long riderId;
    private String addressSnapshot;
    private String contactPhone;
    private LocalDateTime appointmentTime;
    private LocalDateTime payTime;
    private Integer payMethod;
    private String remark;
    private LocalDateTime createTime;
    private List<OrderItemVO> items;

    @Data
    public static class OrderItemVO {
        private Long id;
        private Long productId;
        private Long skuId;
        private String specName;     // 下单规格名称
        private String productName;  // 下单商品名称
        private String productImage; // 下单商品图片
        private BigDecimal price;    // 成交价
        private Integer quantity;
    }
}
