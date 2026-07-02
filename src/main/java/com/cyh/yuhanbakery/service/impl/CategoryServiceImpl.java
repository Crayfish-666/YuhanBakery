package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.entity.Category;
import com.cyh.yuhanbakery.mapper.CategoryMapper;
import com.cyh.yuhanbakery.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
