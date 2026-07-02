package com.cyh.yuhanbakery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ComboSaveDTO {
    private Long id; // 编辑时传输，新增时不传

    @NotBlank(message = "套餐名称不能为空")
    private String name;

    private String description;
    private String image;

    @NotNull(message = "套餐价格不能为空")
    @DecimalMin(value = "0.01", message = "套餐价格必须大于 0")
    private BigDecimal price;

    private Integer status; // 0-下架，1-上架

    @NotEmpty(message = "套餐必须包含至少一个分组")
    @Valid
    private List<ComboGroupDTO> groups;
}
