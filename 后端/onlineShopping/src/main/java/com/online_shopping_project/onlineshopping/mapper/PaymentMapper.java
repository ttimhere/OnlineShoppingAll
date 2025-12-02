package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.Payment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
/**
 * PaymentMapper 接口
 * 负责操作支付记录表（payment）
 * 用于保存每笔订单的支付流水信息。
 */
@Mapper
public interface PaymentMapper {
    //插入一条支付记录，每当用户发起支付请求时，系统会在 payment 表中记录一条支付流水。
    @Insert("INSERT INTO payment(order_no, pay_channel, request_no, amount, status, created_at) " +
            "VALUES(#{orderNo}, #{payChannel}, #{requestNo}, #{amount}, #{status}, NOW())")
    int insertPayment(@Param("orderNo") String orderNo, //订单号
                      @Param("payChannel") String payChannel,  //支付渠道
                      @Param("requestNo") String requestNo, //支付请求号
                      @Param("amount") BigDecimal amount, //支付金额
                      @Param("status") Integer status); //支付状态

    // 据订单号查询支付信息
    @Select("SELECT id, order_no, pay_channel, request_no, amount, status, notify_payload, created_at " +
            "FROM payment " +
            "WHERE order_no = #{orderNo} " +
            "LIMIT 1")
    Payment selectByOrderNo(@Param("orderNo") String orderNo);
}