package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
//AdminOrderMapper负责管理员后台的订单查询、发货、取消等操作，主要操作 order 表
@Mapper
public interface AdminOrderMapper {

    // 订单字段映射配置
    @Results(id = "AdminOrderMap", value = {
            // 基础字段映射（id 作为主键）
            @Result(column = "id",             property = "id", id = true),
            @Result(column = "order_no",       property = "orderNo"),
            @Result(column = "user_id",        property = "userId"),
            // 金额信息
            @Result(column = "total_amount",   property = "totalAmount"),
            @Result(column = "pay_amount",     property = "payAmount"),
            // 状态 & 支付类型
            @Result(column = "status",         property = "status"),
            @Result(column = "pay_type",       property = "payType"),
            // 各类时间戳
            @Result(column = "create_time",    property = "createTime"),
            @Result(column = "pay_time",       property = "payTime"),
            @Result(column = "ship_time",      property = "shipTime"),
            @Result(column = "finish_time",    property = "finishTime"),
            @Result(column = "cancel_time",    property = "cancelTime"),
            @Result(column = "address_snapshot", property = "addressSnapshot")
    })

    //据订单 ID 查询订单  LIMIT 1 保障只返回 1 条记录
    @Select("""
        SELECT *
        FROM `order`
        WHERE id = #{id}
        LIMIT 1
    """)
    Order selectById(Long id);

    // 后台订单多条件查询
    @Select("""
    <script>
    SELECT *
    FROM `order` o
    WHERE 1 = 1
        <if test="status != null">
            AND o.status = #{status}
        </if>
        <if test="userId != null">
            AND o.user_id = #{userId}
        </if>
        <if test="orderNo != null and orderNo != ''">
            AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')
        </if>
        <if test="productTitle != null and productTitle != ''">
            AND EXISTS (
                SELECT 1 FROM order_item oi
                WHERE oi.order_id = o.id
                  AND oi.product_title LIKE CONCAT('%', #{productTitle}, '%')
            )
        </if>

    ORDER BY o.create_time DESC
    </script>
""")
    @ResultMap("AdminOrderMap")
    List<Order> selectByCondition(
            @Param("status") Integer status,
            @Param("userId") Long userId,
            @Param("orderNo") String orderNo,
            @Param("productTitle") String productTitle
    );


    // 发货操作,仅允许状态为 1（已支付，待发货）的订单执行发货
    //发货后状态更新为 2（已发货）,更新 ship_time 为当前时间
    @Update("""
        UPDATE `order`
        SET status = 2,
            ship_time = NOW()
        WHERE id = #{orderId}
          AND status = 1
        """)
    int markShipped(Long orderId);

    // 取消订单,仅允许取消状态为 0（未支付）、1（已支付但未发货）的订单
    //更新状态为 4（已取消）
    @Update("""
        UPDATE `order`
        SET status = 4,
            cancel_time = NOW()
        WHERE id = #{orderId}
          AND status IN (0, 1)
        """)
    int markCancelled(Long orderId);









}
