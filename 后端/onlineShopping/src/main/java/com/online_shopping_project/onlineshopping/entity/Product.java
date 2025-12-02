package com.online_shopping_project.onlineshopping.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product implements Serializable {
    private Long id;  // 商品ID
    private Long categoryId; // 所属类目ID
    private String title; // 商品标题
    private String subTitle; // 商品副标题
    private BigDecimal price; // 商品价格
    private Integer stock;  // 库存数量
    private Integer status;  // 状态（1：上架，0：下架）
    private String mainImg;   // 主图路径
    private String attrs;    // 属性（JSON字符串形式）
    private LocalDateTime createdAt;// 创建时间
    private LocalDateTime updatedAt;// 更新时间
}
