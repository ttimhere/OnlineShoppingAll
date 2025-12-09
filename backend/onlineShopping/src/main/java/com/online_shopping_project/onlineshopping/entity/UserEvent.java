package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.time.LocalDateTime;
//用户行为日志表实体，对应 user_event 表
@Data
public class UserEvent {
    private Long id;
    private Long userId; //关联用户ID
    private String eventType; //事件类型
    private String content; //JSON 文本内容，记录行为上下文
    private LocalDateTime createdAt; //创建时间
}
