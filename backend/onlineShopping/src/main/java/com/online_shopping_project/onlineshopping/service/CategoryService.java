package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.entity.dto.CategoryNode;
import java.util.List;
public interface CategoryService {
    // 查询类目树
    List<CategoryNode> getCategoryTree();
}
