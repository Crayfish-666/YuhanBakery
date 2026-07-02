package com.cyh.yuhanbakery.config;

import com.cyh.yuhanbakery.entity.ProductSku;
import com.cyh.yuhanbakery.mapper.ProductSkuMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目启动时，自动将数据库中的商品库存预热加载到 Redis 中
 * 解决因 data.sql 初始化数据导致 Redis 中缺少库存 Key 从而下单失败的问题
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStockInitRunner implements CommandLineRunner {

    private final ProductSkuMapper productSkuMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>> 开始将数据库中的商品 SKU 库存预热加载至 Redis <<<");
        List<ProductSku> skuList = productSkuMapper.selectList(null);
        if (skuList != null && !skuList.isEmpty()) {
            for (ProductSku sku : skuList) {
                String redisKey = "bakery:sku:stock:" + sku.getId();
                stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(sku.getStock()));
            }
            log.info(">>> 成功预热 {} 个 SKU 库存至 Redis <<<", skuList.size());
        } else {
            log.warn(">>> 数据库中没有商品 SKU 数据，跳过库存预热 <<<");
        }
    }
}
