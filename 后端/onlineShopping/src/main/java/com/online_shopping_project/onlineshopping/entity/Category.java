package com.online_shopping_project.onlineshopping.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class Category implements Serializable {
    private Long id; // 类目ID，主键
    private Long parentId;   // 父类目ID，若为空则为一级类目
    private String name; // 类目名称
    private Integer level; // 类目层级（1为一级，2为二级，以此类推）
    private Integer sort;  // 排序字段，用于前端展示顺序
    private Integer status;    // 状态：1为启用，0为禁用
}