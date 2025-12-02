package com.online_shopping_project.onlineshopping.entity;

import lombok.Data;

//管理员 - 角色关联
@Data
public class AdminUserRole {
    private Long id;
    private Long adminUserId; //管理员id，与 AdminUser 表关联
    private Long roleId; //角色id，与 Role 表关联
}
