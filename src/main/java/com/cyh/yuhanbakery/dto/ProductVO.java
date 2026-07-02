package com.cyh.yuhanbakery.dto;

import com.cyh.yuhanbakery.entity.ProductSku;
import lombok.Data;
import java.util.List;

@Data
public class ProductVO {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private String image;
    private Integer status;
    private List<ProductSku> skus; // 该商品下的所有启用规格 SKU 列表
}
