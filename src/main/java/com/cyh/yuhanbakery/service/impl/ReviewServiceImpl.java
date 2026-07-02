package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.common.BusinessException;
import com.cyh.yuhanbakery.entity.Orders;
import com.cyh.yuhanbakery.entity.ProductReview;
import com.cyh.yuhanbakery.mapper.OrdersMapper;
import com.cyh.yuhanbakery.mapper.ProductReviewMapper;
import com.cyh.yuhanbakery.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl extends ServiceImpl<ProductReviewMapper, ProductReview> implements ReviewService {

    private final OrdersMapper ordersMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReview(Long userId, ProductReview review) {
        // 校验订单是否属于该用户且已完成
        Orders order = ordersMapper.selectOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getOrderNo, review.getOrderNo())
                .eq(Orders::getUserId, userId));
        
        if (order == null) {
            throw new BusinessException("订单不存在或无权评价");
        }
        if (order.getStatus() != 3) {
            throw new BusinessException("仅已完成的订单可以评价");
        }
        
        // 校验是否已评价过该商品（简单防重）
        Long count = this.count(new LambdaQueryWrapper<ProductReview>()
                .eq(ProductReview::getOrderNo, review.getOrderNo())
                .eq(ProductReview::getProductId, review.getProductId()));
        if (count > 0) {
            throw new BusinessException("您已评价过该商品");
        }

        review.setUserId(userId);
        if (review.getRating() == null) {
            review.setRating(5);
        }
        this.save(review);
    }

    @Override
    public List<ProductReview> getReviewsByProductId(Long productId) {
        return this.list(new LambdaQueryWrapper<ProductReview>()
                .eq(ProductReview::getProductId, productId)
                .orderByDesc(ProductReview::getCreateTime));
    }
}
