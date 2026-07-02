package com.cyh.yuhanbakery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyh.yuhanbakery.entity.ProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {

    @Update("UPDATE product_sku SET stock = stock - #{qty} WHERE id = #{skuId} AND stock >= #{qty} AND is_deleted = 0")
    int deductStock(@Param("skuId") Long skuId, @Param("qty") Integer qty);

    @Update("UPDATE product_sku SET stock = stock + #{qty} WHERE id = #{skuId} AND is_deleted = 0")
    int addStock(@Param("skuId") Long skuId, @Param("qty") Integer qty);
}
