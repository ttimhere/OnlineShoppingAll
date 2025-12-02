package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
//用户个人中心视图对象
@Data
public class UserProfileVO implements Serializable {
    private Long id;                   // 用户ID
    private String email;              // 邮箱
    private String nickname;           // 昵称（可空）
    // 订单摘要列表
    private List<UserOrderSummaryVO> orders;
}
