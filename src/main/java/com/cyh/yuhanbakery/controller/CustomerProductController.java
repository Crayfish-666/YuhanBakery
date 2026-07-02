package com.cyh.yuhanbakery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyh.yuhanbakery.common.Result;
import com.cyh.yuhanbakery.dto.ComboVO;
import com.cyh.yuhanbakery.dto.ProductVO;
import com.cyh.yuhanbakery.entity.*;
import com.cyh.yuhanbakery.mapper.*;
import com.cyh.yuhanbakery.service.CategoryService;
import com.cyh.yuhanbakery.service.ComboService;
import com.cyh.yuhanbakery.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductSkuMapper productSkuMapper;
    private final ComboService comboService;
    private final ComboGroupMapper comboGroupMapper;
    private final ComboGroupItemMapper comboGroupItemMapper;
    private final ProductMapper productMapper;

    @GetMapping("/category/list")
    public Result<List<Category>> listCategories() {
        List<Category> list = categoryService.list(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSort));
        return Result.success(list);
    }

    @GetMapping("/product/list")
    public Result<IPage<ProductVO>> listProducts(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1) // 只查上架商品
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .like(StringUtils.hasText(keyword), Product::getName, keyword)
                .orderByDesc(Product::getCreateTime);

        productService.page(page, queryWrapper);

        // 级联组装 SKU
        Page<ProductVO> voPage = new Page<>(pageNum, pageSize);
        voPage.setTotal(page.getTotal());

        if (page.getRecords().isEmpty()) {
            voPage.setRecords(Collections.emptyList());
            return Result.success(voPage);
        }

        List<Long> productIds = page.getRecords().stream().map(Product::getId).collect(Collectors.toList());
        List<ProductSku> allSkus = productSkuMapper.selectList(new LambdaQueryWrapper<ProductSku>()
                .in(ProductSku::getProductId, productIds)
                .eq(ProductSku::getStatus, 1)); // 只查启用的 SKU

        Map<Long, List<ProductSku>> skuGroup = allSkus.stream().collect(Collectors.groupingBy(ProductSku::getProductId));

        List<ProductVO> voList = page.getRecords().stream().map(p -> {
            ProductVO vo = new ProductVO();
            vo.setId(p.getId());
            vo.setCategoryId(p.getCategoryId());
            vo.setName(p.getName());
            vo.setDescription(p.getDescription());
            vo.setImage(p.getImage());
            vo.setStatus(p.getStatus());
            vo.setSkus(skuGroup.getOrDefault(p.getId(), Collections.emptyList()));
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @GetMapping("/product/{id}")
    public Result<ProductVO> getProductDetail(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        if (product == null || product.getStatus() == 0) {
            return Result.failed("商品不存在或已下架");
        }

        List<ProductSku> skus = productSkuMapper.selectList(new LambdaQueryWrapper<ProductSku>()
                .eq(ProductSku::getProductId, id)
                .eq(ProductSku::getStatus, 1));

        ProductVO vo = new ProductVO();
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setDescription(product.getDescription());
        vo.setImage(product.getImage());
        vo.setStatus(product.getStatus());
        vo.setSkus(skus);

        return Result.success(vo);
    }

    private final com.cyh.yuhanbakery.service.ReviewService reviewService;

    @GetMapping("/product/reviews/{productId}")
    public Result<List<ProductReview>> getProductReviews(@PathVariable("productId") Long productId) {
        return Result.success(reviewService.getReviewsByProductId(productId));
    }

    @GetMapping("/combo/list")
    public Result<List<ComboVO>> listCombos() {
        // 1. 查询所有上架的套餐
        List<Combo> combos = comboService.list(new LambdaQueryWrapper<Combo>()
                .eq(Combo::getStatus, 1)
                .orderByDesc(Combo::getCreateTime));
        if (combos.isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        List<ComboVO> voList = new ArrayList<>();
        for (Combo combo : combos) {
            ComboVO vo = new ComboVO();
            vo.setId(combo.getId());
            vo.setName(combo.getName());
            vo.setDescription(combo.getDescription());
            vo.setImage(combo.getImage());
            vo.setPrice(combo.getPrice());
            vo.setStatus(combo.getStatus());

            // 2. 查询套餐包含的配置组
            List<ComboGroup> groups = comboGroupMapper.selectList(new LambdaQueryWrapper<ComboGroup>()
                    .eq(ComboGroup::getComboId, combo.getId()));
            List<ComboVO.ComboGroupVO> groupVOList = new ArrayList<>();

            for (ComboGroup group : groups) {
                ComboVO.ComboGroupVO gVo = new ComboVO.ComboGroupVO();
                gVo.setId(group.getId());
                gVo.setGroupName(group.getGroupName());
                gVo.setChooseCount(group.getChooseCount());

                // 3. 查询分组内的可选 SKU 并拉取 SKU 属性
                List<ComboGroupItem> items = comboGroupItemMapper.selectList(new LambdaQueryWrapper<ComboGroupItem>()
                        .eq(ComboGroupItem::getGroupId, group.getId()));
                List<ComboVO.ComboGroupItemVO> itemVOList = new ArrayList<>();

                if (!items.isEmpty()) {
                    List<Long> skuIds = items.stream().map(ComboGroupItem::getSkuId).collect(Collectors.toList());
                    List<ProductSku> skus = productSkuMapper.selectBatchIds(skuIds);
                    Map<Long, ProductSku> skuMap = skus.stream().collect(Collectors.toMap(ProductSku::getId, s -> s));

                    List<Long> productIds = skus.stream().map(ProductSku::getProductId).distinct().collect(Collectors.toList());
                    List<Product> products = productMapper.selectBatchIds(productIds);
                    Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));

                    for (ComboGroupItem item : items) {
                        ProductSku sku = skuMap.get(item.getSkuId());
                        if (sku == null || sku.getStatus() == 0) {
                            continue; // 过滤禁用规格
                        }
                        Product prod = productMap.get(sku.getProductId());

                        ComboVO.ComboGroupItemVO iVo = new ComboVO.ComboGroupItemVO();
                        iVo.setId(item.getId());
                        iVo.setSkuId(item.getSkuId());
                        iVo.setExtraPrice(item.getExtraPrice());
                        iVo.setSpecName(sku.getSpecName());
                        iVo.setOriginalPrice(sku.getPrice());
                        iVo.setProductName(prod != null ? prod.getName() : "");
                        itemVOList.add(iVo);
                    }
                }
                gVo.setItems(itemVOList);
                groupVOList.add(gVo);
            }
            vo.setGroups(groupVOList);
            voList.add(vo);
        }
        return Result.success(voList);
    }
}
