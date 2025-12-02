package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

//用户订单列表摘要视图对象
@Data
public class UserOrderSummaryVO implements Serializable {

    private Long id;     // 订单主键ID
    private String orderNo;  // 订单编号
    private BigDecimal totalAmount;// 订单总金额
    private BigDecimal payAmount; // 实际支付金额
    private Integer status;  // 订单状态
    private String payType;  // 支付方式

    private LocalDateTime createTime; // 下单时间
    private LocalDateTime payTime;  // 支付时间
    private LocalDateTime shipTime;  // 发货时间
    private LocalDateTime finishTime; // 完成时间
    private LocalDateTime cancelTime; // 取消时间
}
