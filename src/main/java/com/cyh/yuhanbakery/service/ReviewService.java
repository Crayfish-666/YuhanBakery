package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.entity.ProductReview;

import java.util.List;

public interface ReviewService extends IService<ProductReview> {
    void addReview(Long userId, ProductReview review);
    List<ProductReview> getReviewsByProductId(Long productId);
}
