package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Payment implements Serializable {
    private Long id;
    private String orderNo;// 订单编号
    private String payChannel;// 支付渠道标识
    private String requestNo;// 支付请求号
    private BigDecimal amount;// 支付金额
    private Integer status;// 支付状态
    private String notifyPayload; // 支付回调原始报文（JSON字符串）
    private LocalDateTime createdAt;// 支付记录创建时间
}
