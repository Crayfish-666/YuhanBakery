package com.cyh.yuhanbakery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ComboGroupDTO {
    private Long id;

    @NotBlank(message = "分组名称不能为空")
    private String groupName;

    @NotNull(message = "最少或最多可选数量不能为空")
    @Min(value = 1, message = "可选数量至少为 1")
    private Integer chooseCount;

    @NotEmpty(message = "分组下的 SKU 可选项不能为空")
    @Valid
    private List<ComboGroupItemDTO> items;
}
