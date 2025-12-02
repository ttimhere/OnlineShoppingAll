package com.online_shopping_project.onlineshopping.controller;

import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.entity.dto.CategoryNode;
import com.online_shopping_project.onlineshopping.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    /**
     * 获取类目树结构
     * 返回格式：Result<List<CategoryNode>>
     */
    @GetMapping("/tree")
    public Result<List<CategoryNode>> tree() {
        return Result.success(categoryService.getCategoryTree());
    }
}

