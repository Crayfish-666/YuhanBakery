package com.cyh.yuhanbakery.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Long skuId;
    private Long productId;
    private String name;       // 商品名
    private String specName;   // 规格名
    private String image;      // 图片
    private BigDecimal price;  // 单价
    private Integer quantity;  // 购物车内数量
    private Integer stock;     // 当前库存
    private Integer status;    // 状态 (0-下架/禁用，1-正常)
}
