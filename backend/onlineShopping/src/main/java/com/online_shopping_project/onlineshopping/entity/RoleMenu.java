package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
//角色 - 菜单权限关联
@Data
public class RoleMenu {
    private Long id;
    private Long roleId; //角色id
    private Long menuId; //菜单权限id
}
