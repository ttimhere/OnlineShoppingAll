package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;

//菜单权限实体
@Data
public class Menu {
    private Long id;
    private String name;   // 名称
    private String path;  // 前端路由路径
    private String permission;  // 权限字符串
    private Long parentId;
}
