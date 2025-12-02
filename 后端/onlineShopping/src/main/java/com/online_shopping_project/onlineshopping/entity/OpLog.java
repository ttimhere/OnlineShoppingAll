package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.time.LocalDateTime;
//后台操作日志
@Data
public class OpLog {
    private Long id;
    private Long adminUserId; // 操作人
    private String action;   // 操作描述
    private String target;   // 针对对象
    private LocalDateTime createdAt;
}
