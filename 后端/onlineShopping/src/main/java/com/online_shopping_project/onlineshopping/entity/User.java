package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * 用户实体类，对应数据库中的 user 表。
 * 包含用户的基本信息与账户状态字段。
 */
@Data
public class User {
    private Long id;  // 用户ID
    private String email;  // 邮箱（唯一）
    private String phone;  // 手机号
    private String passwordHash;  // 密码哈希值（BCrypt加密）
    private String nickname;  // 昵称
    private Integer status;   // 状态：1启用 / 0禁用
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}