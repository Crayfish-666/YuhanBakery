package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.dto.ProductSaveDTO;
import com.cyh.yuhanbakery.entity.Product;

public interface ProductService extends IService<Product> {
    void saveProductWithSkus(ProductSaveDTO dto);
    void deleteProduct(Long id);
}
