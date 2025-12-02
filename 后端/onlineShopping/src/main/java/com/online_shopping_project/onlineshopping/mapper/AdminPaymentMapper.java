package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.Payment;
import org.apache.ibatis.annotations.*;
//后台支付记录查询 Mapper，用于管理员查看订单对应的支付信息
@Mapper
public interface AdminPaymentMapper {
    // Payment 字段映射
    @Results(id = "AdminPaymentMap", value = {
            @Result(column = "id",           property = "id", id = true),
            @Result(column = "order_no",     property = "orderNo"),
            @Result(column = "pay_channel",  property = "payChannel"),
            @Result(column = "request_no",   property = "requestNo"),
            @Result(column = "amount",       property = "amount"),
            @Result(column = "status",       property = "status"),
            @Result(column = "notify_payload", property = "notifyPayload"),
            @Result(column = "created_at",   property = "createdAt")
    })
    //根据订单号查询支付记录
    @Select("""
        SELECT *
        FROM payment
        WHERE order_no = #{orderNo}
        LIMIT 1
        """)
    Payment selectByOrderNo(String orderNo);
}
