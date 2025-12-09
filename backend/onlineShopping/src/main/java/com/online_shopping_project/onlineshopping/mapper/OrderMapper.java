package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.Order;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;
/**
 * OrderMapper 接口
 * 负责操作数据库中的 order`表
 * 使用 MyBatis 注解方式定义 SQL 映射。
 */
@Mapper
public interface OrderMapper {
    //插入新订单记录
    @Insert("""
    INSERT INTO `order`(
        order_no, user_id, total_amount, pay_amount,
        status, pay_type, create_time, pay_time
    )
    VALUES(
        #{orderNo}, #{userId}, #{totalAmount}, #{payAmount},
        #{status}, #{payType}, NOW(), NULL
    )
    """)
    int insertOrder(@Param("orderNo") String orderNo, //订单编号
                    @Param("userId") Long userId, // 用户ID
                    @Param("totalAmount") BigDecimal totalAmount, //订单总金额
                    @Param("payAmount") BigDecimal payAmount, //实际支付金额
                    @Param("status") Integer status, //订单状态
                    @Param("payType") String payType); // 支付方式
    //根据订单号查询订单ID
    @Select("SELECT id FROM `order` WHERE order_no = #{orderNo} LIMIT 1")
    Long selectIdByOrderNo(@Param("orderNo") String orderNo); //订单编号
    //标记订单为已支付，更新订单状态 ，同时更新支付时间 pay_time = 当前时间
    @Update("UPDATE `order` SET status = 1, pay_time = NOW() WHERE order_no = #{orderNo}")
    int markPaid(@Param("orderNo") String orderNo); //订单编号
    // 按用户ID查询该用户所有订单
    @Select("SELECT id, order_no, user_id, total_amount, pay_amount, status, pay_type, " +
            "create_time, pay_time, ship_time, finish_time, cancel_time, address_snapshot " +
            "FROM `order` " +
            "WHERE user_id = #{userId} " +
            "ORDER BY create_time DESC")
    List<Order> listByUserId(@Param("userId") Long userId);
    // 根据订单ID和用户ID查询订单，防止越权
    @Select("SELECT id, order_no, user_id, total_amount, pay_amount, status, pay_type, " +
            "create_time, pay_time, ship_time, finish_time, cancel_time, address_snapshot " +
            "FROM `order` " +
            "WHERE id = #{id} AND user_id = #{userId} " +
            "LIMIT 1")
    Order selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    // 用户确认收货：仅允许状态为 2（已发货）的订单
    @Update("""
    UPDATE `order`
    SET status = 3,
        finish_time = NOW()
    WHERE id = #{orderId}
      AND user_id = #{userId}
      AND status = 2
    """)
    int markFinished(@Param("orderId") Long orderId,
                     @Param("userId") Long userId);

    // 更新订单的收货地址快照（JSON）
    @Update("""
    UPDATE `order`
    SET address_snapshot = #{addressSnapshot}
    WHERE id = #{orderId}
""")
    int updateAddressSnapshot(@Param("orderId") Long orderId,
                              @Param("addressSnapshot") String addressSnapshot);
}

