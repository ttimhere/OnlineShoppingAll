package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class OrderItem implements Serializable {
    private Long id;
    private Long orderId;// 所属订单ID，关联Order.id
    private Long productId;// 商品ID
    private String productTitle;// 下单时的商品标题
    private BigDecimal price;// 下单时商品单价
    private Integer quantity;// 购买数量
    private String img;// 商品主图
}
