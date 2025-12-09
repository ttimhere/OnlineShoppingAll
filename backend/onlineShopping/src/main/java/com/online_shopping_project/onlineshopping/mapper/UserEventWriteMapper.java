package com.online_shopping_project.onlineshopping.mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * 前台用户行为日志写入 Mapper
 */
@Mapper
public interface UserEventWriteMapper {

    @Insert("""
        INSERT INTO user_event (user_id, event_type, content, created_at)
        VALUES (#{userId}, #{eventType}, #{content}, NOW())
        """)
    int insertEvent(
            @Param("userId") Long userId,
            @Param("eventType") String eventType,
            @Param("content") String content
    );
}
