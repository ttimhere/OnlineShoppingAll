package com.online_shopping_project.onlineshopping.entity.query;

import lombok.Data;
import java.util.List;

@Data
public class ProductQuery {
    private Long categoryId;  // 类目筛选
    private String keyword; // 关键字（title / sub_title）
    private String orderBy;  // 排序
    private List<Long> categoryIds;
}