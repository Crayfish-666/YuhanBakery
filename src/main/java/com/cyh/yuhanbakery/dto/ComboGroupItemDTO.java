package com.cyh.yuhanbakery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ComboGroupItemDTO {
    private Long id;

    @NotNull(message = "关联 SKU 不能为空")
    private Long skuId;

    @NotNull(message = "换购溢价金额不能为空")
    private BigDecimal extraPrice;
}
