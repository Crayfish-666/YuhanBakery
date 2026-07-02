package com.cyh.yuhanbakery.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductSkuDTO {
    private Long id; // 如果有 id 代表是已有 SKU 修改，否则代表是新增

    @NotBlank(message = "规格名称不能为空")
    private String specName;

    @NotNull(message = "规格价格不能为空")
    @DecimalMin(value = "0.01", message = "规格价格必须大于 0")
    private BigDecimal price;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能为负数")
    private Integer stock;

    private Integer status; // 0-禁用，1-启用
}
