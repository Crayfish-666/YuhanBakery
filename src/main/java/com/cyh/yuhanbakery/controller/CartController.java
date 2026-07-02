package com.cyh.yuhanbakery.controller;

import com.cyh.yuhanbakery.common.Result;
import com.cyh.yuhanbakery.dto.CartItemVO;
import com.cyh.yuhanbakery.entity.Product;
import com.cyh.yuhanbakery.entity.ProductSku;
import com.cyh.yuhanbakery.mapper.ProductMapper;
import com.cyh.yuhanbakery.mapper.ProductSkuMapper;
import com.cyh.yuhanbakery.security.CustomUserDetails;
import com.cyh.yuhanbakery.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductSkuMapper productSkuMapper;
    private final ProductMapper productMapper;

    private Long getCurrentUserId() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    @PostMapping("/add")
    public Result<?> addCart(@RequestParam("skuId") Long skuId, @RequestParam("quantity") Integer quantity) {
        cartService.addCart(getCurrentUserId(), skuId, quantity);
        return Result.success(null, "添加购物车成功");
    }

    @GetMapping("/list")
    public Result<List<CartItemVO>> listCart() {
        Long userId = getCurrentUserId();
        Map<Long, Integer> rawCart = cartService.listCart(userId);
        if (rawCart.isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        // 批量查询 SKU 详情
        List<ProductSku> skus = productSkuMapper.selectBatchIds(rawCart.keySet());
        if (skus.isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        // 批量查询商品详情
        List<Long> productIds = skus.stream().map(ProductSku::getProductId).distinct().collect(Collectors.toList());
        List<Product> products = productMapper.selectBatchIds(productIds);
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));

        List<CartItemVO> voList = new ArrayList<>();
        for (ProductSku sku : skus) {
            Product product = productMap.get(sku.getProductId());
            CartItemVO vo = new CartItemVO();
            vo.setSkuId(sku.getId());
            vo.setProductId(sku.getProductId());
            vo.setName(product != null ? product.getName() : "");
            vo.setSpecName(sku.getSpecName());
            vo.setImage(product != null ? product.getImage() : "");
            vo.setPrice(sku.getPrice());
            vo.setQuantity(rawCart.get(sku.getId()));
            vo.setStock(sku.getStock());
            vo.setStatus((product != null && product.getStatus() == 1 && sku.getStatus() == 1) ? 1 : 0);

            voList.add(vo);
        }

        return Result.success(voList);
    }

    @PutMapping("/quantity")
    public Result<?> updateQuantity(@RequestParam("skuId") Long skuId, @RequestParam("quantity") Integer quantity) {
        cartService.updateQuantity(getCurrentUserId(), skuId, quantity);
        return Result.success(null, "修改数量成功");
    }

    @DeleteMapping("/{skuId}")
    public Result<?> deleteCart(@PathVariable("skuId") Long skuId) {
        cartService.deleteCart(getCurrentUserId(), skuId);
        return Result.success(null, "移除商品成功");
    }

    @DeleteMapping("/clear")
    public Result<?> clearCart() {
        cartService.clearCart(getCurrentUserId());
        return Result.success(null, "清空购物车成功");
    }
}
