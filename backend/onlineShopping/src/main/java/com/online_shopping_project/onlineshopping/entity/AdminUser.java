package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.time.LocalDateTime;
//后台管理员实体
@Data
public class AdminUser {
    private Long id;
    private String username;  // 登录用户名
    private String passwordHash; //密码
    private String nickname;  // 显示名称
    private Integer status;  // 状态码 1 启用 0 禁用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
