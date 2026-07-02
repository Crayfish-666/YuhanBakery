package com.cyh.yuhanbakery.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ComboVO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private Integer status;
    private List<ComboGroupVO> groups;

    @Data
    public static class ComboGroupVO {
        private Long id;
        private String groupName;
        private Integer chooseCount;
        private List<ComboGroupItemVO> items;
    }

    @Data
    public static class ComboGroupItemVO {
        private Long id;
        private Long skuId;
        private BigDecimal extraPrice;
        private String specName;         // 可选规格名
        private BigDecimal originalPrice; // 规格原单价
        private String productName;      // 所属商品名称
    }
}
