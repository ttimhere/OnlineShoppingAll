package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.entity.Order;
import com.online_shopping_project.onlineshopping.entity.OrderDetailVO;
import java.util.List;
//用户个人中心订单服务接口
public interface OrderService {
    //查询某个用户的全部订单（按时间倒序）
    List<Order> listOrdersByUser(Long userId);
    //查询某个用户的指定订单详情，如订单不属于该用户，返回 null
    OrderDetailVO getOrderDetail(Long userId, Long orderId);
    // 用户确认完成订单
    void finishOrder(Long userId, Long orderId);
}
