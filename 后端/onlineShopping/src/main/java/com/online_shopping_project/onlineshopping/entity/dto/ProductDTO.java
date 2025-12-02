package com.online_shopping_project.onlineshopping.entity.dto;

import com.online_shopping_project.onlineshopping.entity.ProductImage;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;  // 商品ID
    private Long categoryId;  // 类目ID
    private String title;  // 标题
    private String subTitle; // 副标题
    private BigDecimal price; // 价格
    private Integer stock;  // 库存
    private String mainImg;  // 主图
    private List<ProductImage> images;  // 商品图片列表
}
