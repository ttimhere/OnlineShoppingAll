package com.online_shopping_project.onlineshopping.mapper;

import com.online_shopping_project.onlineshopping.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 查询所有启用状态的类目
    List<Category> selectAllActive();
    // 根据父类目ID查询子类目// status = 1
    List<Category> selectByParentId(Long pid);    // children
}
