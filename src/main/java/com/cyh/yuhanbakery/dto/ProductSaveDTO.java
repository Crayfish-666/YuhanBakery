package com.cyh.yuhanbakery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductSaveDTO {
    private Long id; // 编辑时传输，新增时不传

    @NotNull(message = "商品分类不能为空")
    private Long categoryId;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    private String description;
    private String image;
    private Integer status; // 0-下架，1-上架

    @NotEmpty(message = "规格列表不能为空")
    @Valid
    private List<ProductSkuDTO> skus;
}
