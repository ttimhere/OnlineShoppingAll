package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.entity.Order;
import com.online_shopping_project.onlineshopping.entity.OrderDetailVO;
import com.online_shopping_project.onlineshopping.entity.OrderItem;
import com.online_shopping_project.onlineshopping.entity.Payment;
import com.online_shopping_project.onlineshopping.mapper.OrderItemMapper;
import com.online_shopping_project.onlineshopping.mapper.OrderMapper;
import com.online_shopping_project.onlineshopping.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
//用户个人中心订单服务实现
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    // 注入 Mapper，用于访问订单、订单项、支付记录
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final PaymentMapper paymentMapper;
    //查询某个用户的全部订单（按时间倒序）
    @Override
    public List<Order> listOrdersByUser(Long userId) {
        return orderMapper.listByUserId(userId);
    }
    //查询某个用户的指定订单详情
    @Override
    public OrderDetailVO getOrderDetail(Long userId, Long orderId) {
        // 先根据订单ID和用户ID查询，确保是当前用户的订单，避免越权
        Order order = orderMapper.selectByIdAndUserId(orderId, userId);
        if (order == null) {
            return null;}
        // 查询订单明细
        List<OrderItem> items = orderItemMapper.listByOrderId(order.getId());
        // 查询支付信息
        Payment payment = paymentMapper.selectByOrderNo(order.getOrderNo());
        //封装为订单详情对象返回
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrder(order);
        vo.setItems(items);
        vo.setPayment(payment);
        return vo;
    }
    // 用户确认完成订单
    @Override
    public void finishOrder(Long userId, Long orderId) {
        // 校验订单是否属于当前用户
        Order order = orderMapper.selectByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("订单不存在");}
        // 校验订单状态是否为已发货
        if (order.getStatus() == null || order.getStatus() != 2) {
            throw new RuntimeException("订单尚未发货，无法确认收货");}
        // 执行状态更新（markFinished）
        int updated = orderMapper.markFinished(orderId, userId);
        if (updated <= 0) {
            throw new RuntimeException("订单状态已变更，确认收货失败");}
    }
}
