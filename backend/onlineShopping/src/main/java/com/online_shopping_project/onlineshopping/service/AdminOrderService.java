package com.online_shopping_project.onlineshopping.service;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.entity.Order;
import com.online_shopping_project.onlineshopping.entity.OrderDetailVO;
import com.online_shopping_project.onlineshopping.entity.UserAddress;

public interface AdminOrderService {
    //后台订单分页查询
    PageInfo<Order> pageOrders(Integer status,
                               Long userId,
                               String orderNo,
                               String productTitle,
                               int pageNum,
                               int pageSize);
    //查看订单详情
    OrderDetailVO getOrderDetail(Long orderId);
    //发货：设置发货时间、状态
    void shipOrder(Long orderId, Long adminUserId);
    //取消订单：更新状态 + 回滚库存 + 写库存流水
    void cancelOrder(Long orderId, Long adminUserId);
    // 更新订单的收货地址
    void updateOrderAddress(Long orderId, String addressJson, Long adminUserId);
}
