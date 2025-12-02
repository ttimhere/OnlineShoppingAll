package com.online_shopping_project.onlineshopping.entity.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryNode {
    private Long id;  // 类目ID
    private Long parentId; // 父类目ID
    private String name;  // 类目名称
    private Integer level;// 层级
    private Integer sort;  // 排序

    private List<CategoryNode> children = new ArrayList<>(); // 子类目列表
}
