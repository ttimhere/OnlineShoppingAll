package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Order implements Serializable {
    private Long id;// 订单主键ID
    private String orderNo;// 订单编号
    private Long userId;// 下单用户ID
    private BigDecimal totalAmount;// 订单总金额（商品原始金额合计）
    private BigDecimal payAmount;// 实际支付金额（可能包含优惠、满减后的金额）
    private Integer status;// 订单状态
    private String payType;// 支付方式
    private LocalDateTime createTime;// 下单时间
    private LocalDateTime payTime;// 支付成功时间
    private LocalDateTime shipTime;// 发货时间
    private LocalDateTime finishTime;// 订单完成时间
    private LocalDateTime cancelTime;// 订单取消时间
    private String addressSnapshot;// 收货地址
}
