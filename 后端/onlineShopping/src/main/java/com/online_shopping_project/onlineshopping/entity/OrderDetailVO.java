package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

// 后台订单详情视图对象
@Data
public class OrderDetailVO implements Serializable {
    private Order order;// 订单主信息，包含状态、金额、时间节点等
    private java.util.List<OrderItem> items;// 订单商品明细列表
    private Payment payment;// 支付信息
}
