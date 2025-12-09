package com.online_shopping_project.onlineshopping.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProductImage implements Serializable {
    private Long id; // 图片ID
    private Long productId;  // 关联的商品ID
    private String url; // 图片访问URL
    private Integer sort; // 排序字段
}
