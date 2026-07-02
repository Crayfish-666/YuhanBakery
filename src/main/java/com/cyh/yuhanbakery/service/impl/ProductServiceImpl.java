package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.dto.ProductSaveDTO;
import com.cyh.yuhanbakery.dto.ProductSkuDTO;
import com.cyh.yuhanbakery.entity.Product;
import com.cyh.yuhanbakery.entity.ProductSku;
import com.cyh.yuhanbakery.mapper.ProductMapper;
import com.cyh.yuhanbakery.mapper.ProductSkuMapper;
import com.cyh.yuhanbakery.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductWithSkus(ProductSaveDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setCategoryId(dto.getCategoryId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        // 1. 保存或更新商品主表
        if (product.getId() == null) {
            productMapper.insert(product);
        } else {
            productMapper.updateById(product);
        }

        Long productId = product.getId();

        // 2. 加载该商品在数据库中已存在的所有 SKU 列表
        List<ProductSku> existingSkus = productSkuMapper.selectList(new LambdaQueryWrapper<ProductSku>()
                .eq(ProductSku::getProductId, productId));
        Map<Long, ProductSku> existingSkuMap = existingSkus.stream()
                .collect(Collectors.toMap(ProductSku::getId, s -> s));

        Set<Long> incomingSkuIds = new HashSet<>();

        // 3. 处理传入的 SKU
        for (ProductSkuDTO skuDto : dto.getSkus()) {
            ProductSku sku = new ProductSku();
            sku.setProductId(productId);
            sku.setSpecName(skuDto.getSpecName());
            sku.setPrice(skuDto.getPrice());
            sku.setStock(skuDto.getStock());
            sku.setStatus(skuDto.getStatus() != null ? skuDto.getStatus() : 1);

            if (skuDto.getId() != null && existingSkuMap.containsKey(skuDto.getId())) {
                // 更新已存在的 SKU 规格
                sku.setId(skuDto.getId());
                productSkuMapper.updateById(sku);
                incomingSkuIds.add(sku.getId());
            } else {
                // 新增规格 SKU
                productSkuMapper.insert(sku);
                incomingSkuIds.add(sku.getId());
            }

            // 同步将最新库存热加载至 Redis 缓存中，供高并发下单 Lua 脚本快速读取与扣减
            String redisStockKey = "bakery:sku:stock:" + sku.getId();
            redisTemplate.opsForValue().set(redisStockKey, String.valueOf(sku.getStock()));
            log.info("热加载商品 SKU 库存至 Redis：key={}, stock={}", redisStockKey, sku.getStock());
        }

        // 4. 软删除数据库中已去除的旧规格，并清理缓存中对应的库存 Key
        for (ProductSku oldSku : existingSkus) {
            if (!incomingSkuIds.contains(oldSku.getId())) {
                productSkuMapper.deleteById(oldSku.getId()); // 逻辑删除
                String redisStockKey = "bakery:sku:stock:" + oldSku.getId();
                redisTemplate.delete(redisStockKey);
                log.info("规格被废弃，已清除 Redis 中的库存 Key：{}", redisStockKey);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        // 1. 逻辑删除商品主表
        productMapper.deleteById(id);

        // 2. 逻辑删除关联的全部 SKU 表，并同步清理 Redis 库存
        List<ProductSku> skus = productSkuMapper.selectList(new LambdaQueryWrapper<ProductSku>()
                .eq(ProductSku::getProductId, id));

        for (ProductSku sku : skus) {
            productSkuMapper.deleteById(sku.getId());
            String redisStockKey = "bakery:sku:stock:" + sku.getId();
            redisTemplate.delete(redisStockKey);
            log.info("商品下架/删除，同步删除 Redis 库存：{}", redisStockKey);
        }
    }
}
