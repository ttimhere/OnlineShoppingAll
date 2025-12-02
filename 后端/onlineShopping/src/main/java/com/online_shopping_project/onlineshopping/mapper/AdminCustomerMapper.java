package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.CustomerSummaryVO;
import org.apache.ibatis.annotations.*;
import java.util.List;
//后台客户管理相关查询
@Mapper
public interface AdminCustomerMapper {
    /**
     * 客户列表和基本统计（订单数、总消费）
     * - 订单数与总消费金额只统计 status IN (1,2,3) 的有效订单
     * - 支持按 email / phone / nickname 关键字模糊查询
     */
    @Select("""
        <script>
        SELECT
            u.id                         AS userId,
            u.email                      AS email,
            u.phone                      AS phone,
            u.nickname                   AS nickname,
            u.status                     AS status,
            u.created_at                 AS createdAt,
            IFNULL(COUNT(o.id), 0)       AS orderCount,
            IFNULL(SUM(o.pay_amount), 0) AS totalAmount
        FROM `user` u
        LEFT JOIN `order` o
          ON o.user_id = u.id
         AND o.status IN (1,2,3)
        <where>
            <if test="keyword != null and keyword != ''">
                AND (
                    u.email    LIKE CONCAT('%', #{keyword}, '%')
                    OR u.phone LIKE CONCAT('%', #{keyword}, '%')
                    OR u.nickname LIKE CONCAT('%', #{keyword}, '%')
                )
            </if>
        </where>
        GROUP BY u.id, u.email, u.phone, u.nickname, u.status, u.created_at
        ORDER BY u.created_at DESC
        </script>
        """)
    @Results(id = "CustomerSummaryMap", value = {
            @Result(column = "userId",      property = "userId",    id = true),
            @Result(column = "email",       property = "email"),
            @Result(column = "phone",       property = "phone"),
            @Result(column = "nickname",    property = "nickname"),
            @Result(column = "status",      property = "status"),
            @Result(column = "createdAt",   property = "createdAt"),
            @Result(column = "orderCount",  property = "orderCount"),
            @Result(column = "totalAmount", property = "totalAmount")
    })
    List<CustomerSummaryVO> listCustomerSummary(@Param("keyword") String keyword);
}
