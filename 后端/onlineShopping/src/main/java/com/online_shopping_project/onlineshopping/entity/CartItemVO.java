package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Long cartItemId; // 购物车商品ID
    private Long productId; // 商品ID
    private String title; //商品标题
    private String subTitle; // 商品副标题
    private String img; //图片路径
    private BigDecimal price; // 商品价格
    private Integer quantity; //数目
    private Boolean selected; //是否选中
    private BigDecimal subtotal; // price * quantity
    private Integer stock;  // 展示可用库存
}
