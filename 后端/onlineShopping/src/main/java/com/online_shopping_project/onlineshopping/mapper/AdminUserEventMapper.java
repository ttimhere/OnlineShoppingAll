package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.UserEvent;
import org.apache.ibatis.annotations.*;
import java.util.List;
//后台查看 user_event 用户行为日志
@Mapper
public interface AdminUserEventMapper {
    /**
     * 按条件查询用户行为日志
     * - 支持按 userId、eventType 过滤
     * - 具体分页由 Service 层 PageHelper 处理
     */
    @Select("""
        <script>
        SELECT
            id,
            user_id    AS userId,
            event_type AS eventType,
            content,
            created_at AS createdAt
        FROM user_event
        <where>
            <if test="userId != null">
                AND user_id = #{userId}
            </if>
            <if test="eventType != null and eventType != ''">
                AND event_type = #{eventType}
            </if>
        </where>
        ORDER BY created_at DESC
        </script>
        """)
    @Results(id = "UserEventMap", value = {
            @Result(column = "id",         property = "id",        id = true),
            @Result(column = "userId",     property = "userId"),
            @Result(column = "eventType",  property = "eventType"),
            @Result(column = "content",    property = "content"),
            @Result(column = "createdAt",  property = "createdAt")
    })
    List<UserEvent> queryUserEvents(@Param("userId") Long userId,
                                    @Param("eventType") String eventType);
}
