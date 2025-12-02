package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
//后台客户列表行数据：用户基本信息 + 订单数 + 总消费金额
@Data
public class CustomerSummaryVO {
    private Long userId; //用户ID
    private String email; //邮箱
    private String phone; //手机号
    private String nickname; //昵称
    private Integer status; //状态
    private LocalDateTime createdAt; //注册时间
    private Integer orderCount; //有效订单数量
    private BigDecimal totalAmount; //有效订单总支付金额
}
