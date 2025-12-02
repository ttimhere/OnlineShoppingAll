package com.online_shopping_project.onlineshopping.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItem {
    private Long id;  // 标识购物车项
    private Long userId;  // 用户ID，用于区分不同用户的购物车
    private Long productId; // 商品ID，用于关联商品表中的具体商品信息
    private Integer quantity; // 商品数量
    private Boolean selected; // 是否选中
    private LocalDateTime updatedAt;// 更新时间
    private LocalDateTime createTime;// 创建时间
}
