package com.cyh.yuhanbakery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyh.yuhanbakery.common.BusinessException;
import com.cyh.yuhanbakery.common.Result;
import com.cyh.yuhanbakery.dto.ComboSaveDTO;
import com.cyh.yuhanbakery.dto.ProductSaveDTO;
import com.cyh.yuhanbakery.entity.*;
import com.cyh.yuhanbakery.mapper.OrderItemMapper;
import com.cyh.yuhanbakery.mapper.OrdersMapper;
import com.cyh.yuhanbakery.mapper.ProductSkuMapper;
import com.cyh.yuhanbakery.service.CategoryService;
import com.cyh.yuhanbakery.service.ComboService;
import com.cyh.yuhanbakery.service.OssService;
import com.cyh.yuhanbakery.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductSkuMapper productSkuMapper;
    private final ComboService comboService;
    private final OssService ossService;
    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;

    // ==================== 1. 图片直传 OSS/本地降级接口 ====================
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = ossService.uploadFile(file);
        return Result.success(url, "图片上传成功");
    }

    // ==================== 2. 商品分类管理 CRUD ====================
    @PostMapping("/category")
    public Result<?> saveCategory(@Valid @RequestBody Category category) {
        categoryService.saveOrUpdate(category);
        return Result.success(null, "保存分类成功");
    }

    @DeleteMapping("/category/{id}")
    public Result<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.removeById(id);
        return Result.success(null, "删除分类成功");
    }

    @GetMapping("/category/list")
    public Result<List<Category>> listCategories() {
        List<Category> list = categoryService.list(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSort));
        return Result.success(list);
    }

    // ==================== 3. 商品与多规格 SKU 级联操作 ====================
    @PostMapping("/product/save")
    public Result<?> saveProduct(@Valid @RequestBody ProductSaveDTO dto) {
        productService.saveProductWithSkus(dto);
        return Result.success(null, "级联保存商品规格成功");
    }

    @DeleteMapping("/product/{id}")
    public Result<?> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return Result.success(null, "级联删除商品及规格成功");
    }

    @PutMapping("/product/status/{id}")
    public Result<?> toggleProductStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        Product p = productService.getById(id);
        if (p == null) {
            throw new BusinessException("商品不存在");
        }
        p.setStatus(status);
        productService.updateById(p);
        return Result.success(null, "商品上下架状态变更成功");
    }

    @GetMapping("/product/list")
    public Result<IPage<Product>> listProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        Page<Product> page = new Page<>(pageNum, pageSize);
        productService.page(page, new LambdaQueryWrapper<Product>()
                .like(StringUtils.hasText(keyword), Product::getName, keyword)
                .orderByDesc(Product::getCreateTime));
        return Result.success(page);
    }

    // ==================== 4. 套餐及分组关联级联操作 ====================
    @PostMapping("/combo/save")
    public Result<?> saveCombo(@Valid @RequestBody ComboSaveDTO dto) {
        comboService.saveComboWithGroups(dto);
        return Result.success(null, "级联保存套餐成功");
    }

    @DeleteMapping("/combo/{id}")
    public Result<?> deleteCombo(@PathVariable("id") Long id) {
        comboService.deleteCombo(id);
        return Result.success(null, "级联删除套餐成功");
    }

    @PutMapping("/combo/status/{id}")
    public Result<?> toggleComboStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        Combo c = comboService.getById(id);
        if (c == null) {
            throw new BusinessException("套餐不存在");
        }
        c.setStatus(status);
        comboService.updateById(c);
        return Result.success(null, "套餐上下架状态变更成功");
    }

    @GetMapping("/combo/list")
    public Result<IPage<Combo>> listCombos(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        Page<Combo> page = new Page<>(pageNum, pageSize);
        comboService.page(page, new LambdaQueryWrapper<Combo>()
                .like(StringUtils.hasText(keyword), Combo::getName, keyword)
                .orderByDesc(Combo::getCreateTime));
        return Result.success(page);
    }

    // ==================== 5. 管理端全局订单检索 ====================
    @GetMapping("/order/list")
    public Result<IPage<Orders>> listAllOrders(
            @RequestParam(value = "orderNo", required = false) String orderNo,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        
        Page<Orders> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Orders> qw = new LambdaQueryWrapper<Orders>()
                .eq(StringUtils.hasText(orderNo), Orders::getOrderNo, orderNo)
                .eq(status != null, Orders::getStatus, status)
                .orderByDesc(Orders::getCreateTime);
        
        ordersMapper.selectPage(page, qw);
        return Result.success(page);
    }

    // ==================== 6. 数据大屏看板统计图表接口 ====================
    @GetMapping("/dashboard/stats")
    public Result<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // A. 销售额统计 (已支付、配送中、已送达的订单总额)
        List<Orders> paidOrders = ordersMapper.selectList(new LambdaQueryWrapper<Orders>()
                .in(Orders::getStatus, 1, 2, 3));
        BigDecimal totalSales = paidOrders.stream()
                .map(Orders::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalSales", totalSales);

        // B. 各状态订单数量统计
        Long totalOrdersCount = ordersMapper.selectCount(null);
        Long pendingOrders = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>().eq(Orders::getStatus, 0));
        Long paidOrdersCount = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>().eq(Orders::getStatus, 1));
        Long deliveringOrders = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>().eq(Orders::getStatus, 2));
        Long completedOrders = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>().eq(Orders::getStatus, 3));
        Long cancelledOrders = ordersMapper.selectCount(new LambdaQueryWrapper<Orders>().eq(Orders::getStatus, 4));

        stats.put("totalOrders", totalOrdersCount);
        stats.put("pendingPayCount", pendingOrders);
        stats.put("paidCount", paidOrdersCount);
        stats.put("deliveringCount", deliveringOrders);
        stats.put("completedCount", completedOrders);
        stats.put("cancelledCount", cancelledOrders);

        // C. 分类销量大体分布统计 (供柱状图/饼图使用)
        List<Map<String, Object>> categorySales = ordersMapper.getCategorySales();
        Map<String, Object> categoryChart = new HashMap<>();
        List<String> categoryNames = categorySales.stream().map(m -> m.get("name").toString()).toList();
        List<Long> actualSalesData = categorySales.stream().map(m -> Long.parseLong(m.get("value").toString())).toList();

        categoryChart.put("names", categoryNames);
        categoryChart.put("values", actualSalesData);
        stats.put("categoryChart", categoryChart);

        // D. 过去7天的真实流水折线图数据
        List<Map<String, Object>> weeklySalesData = ordersMapper.getWeeklySales();
        stats.put("weeklySales", weeklySalesData);

        return Result.success(stats);
    }
}
