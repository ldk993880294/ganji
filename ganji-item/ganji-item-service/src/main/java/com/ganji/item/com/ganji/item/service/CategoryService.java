package com.ganji.item.com.ganji.item.service;


import com.ganji.item.com.ganji.item.mapper.CategoryMapper;
import com.ganji.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryByPid(Long pid) {
        Category category=new Category();
        category.setParentId(pid);
        return this.categoryMapper.select(category);
    }


}
