package com.online_shopping_project.onlineshopping.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.entity.*;
import com.online_shopping_project.onlineshopping.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
//后台订单管理服务实现类
@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {
    // 注入各类 Mapper，用于访问订单、明细、支付、库存与日志表
    private final AdminOrderMapper adminOrderMapper;
    private final AdminOrderItemMapper adminOrderItemMapper;
    private final AdminPaymentMapper adminPaymentMapper;
    private final AdminStockFlowMapper adminStockFlowMapper;
    private final OpLogMapper opLogMapper;
    private final OrderMapper orderMapper;
    // 状态常量
    private static final int ORDER_STATUS_UNPAID    = 0; // 未支付
    private static final int ORDER_STATUS_PAID      = 1; // 已支付
    private static final int ORDER_STATUS_SHIPPED   = 2;  // 已发货
    private static final int ORDER_STATUS_FINISHED  = 3;  // 已完成
    private static final int ORDER_STATUS_CANCELLED = 4; // 已取消
    //订单分页查询
    @Override
    public PageInfo<Order> pageOrders(Integer status, // 订单状态
                                      Long userId, // 用户ID
                                      String orderNo, //订单号
                                      String productTitle, // 商品标题关键字
                                      int pageNum, // 页码
                                      int pageSize) {
        PageHelper.startPage(pageNum, pageSize); // 开启分页
        // 查询符合条件的订单列表
        List<Order> list = adminOrderMapper.selectByCondition(status, userId, orderNo, productTitle);
        return new PageInfo<>(list);
    }

    //查询订单详情
    @Override
    public OrderDetailVO getOrderDetail(Long orderId) {
        // 查询订单主信息
        Order order = adminOrderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 查询该订单的明细商品列表
        List<OrderItem> items = adminOrderItemMapper.selectByOrderId(orderId);
        // 查询支付记录
        Payment payment = adminPaymentMapper.selectByOrderNo(order.getOrderNo());
        // 封装VO返回给前端
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrder(order);
        vo.setItems(items);
        vo.setPayment(payment);
        return vo;
    }
    // 订单发货
    @Override
    @Transactional
    public void shipOrder(Long orderId, Long adminUserId) {
        // 查询订单信息
        Order order = adminOrderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 只有已支付订单才能发货
        if (order.getStatus() == null || order.getStatus() != ORDER_STATUS_PAID) {
            throw new RuntimeException("只有已支付订单才能发货");
        }
        // 执行发货状态更新
        int updated = adminOrderMapper.markShipped(orderId);
        if (updated <= 0) {
            throw new RuntimeException("订单状态已变更，发货失败");
        }
        // 发货事件写入库存流水
        List<OrderItem> items = adminOrderItemMapper.selectByOrderId(orderId);
        for (OrderItem item : items) {
            adminStockFlowMapper.insertFlow(
                    item.getProductId(),
                    0, // 发货不改变库存，只记录事件
                    "SHIP",// 流水类型
                    order.getOrderNo()
            );
        }
        // 写入后台管理员操作日志
        OpLog log = new OpLog();
        log.setAdminUserId(adminUserId);
        log.setAction("发货订单");
        log.setTarget("order:" + orderId);
        opLogMapper.insert(log);
    }
    //订单取消
    @Override
    @Transactional
    public void cancelOrder(Long orderId, Long adminUserId) {
        // 查询订单
        Order order = adminOrderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 校验订单状态是否合法
        if (order.getStatus() == null) {
            throw new RuntimeException("订单状态异常");
        }
        // 已发货、已完成、已取消 的订单禁止取消
        if (order.getStatus() == ORDER_STATUS_SHIPPED ||
                order.getStatus() == ORDER_STATUS_FINISHED ||
                order.getStatus() == ORDER_STATUS_CANCELLED) {
            throw new RuntimeException("订单已发货/完成/取消，无法再次取消");
        }
        // 执行取消订单
        int updated = adminOrderMapper.markCancelled(orderId);
        if (updated <= 0) {
            throw new RuntimeException("订单状态已变更，取消失败");
        }
        // 回滚库存并写库存流水
        List<OrderItem> items = adminOrderItemMapper.selectByOrderId(orderId);
        for (OrderItem item : items) {
            // 恢复库存
            adminStockFlowMapper.increaseStock(item.getProductId(), item.getQuantity());
            // 库存流水：正数表示归还
            adminStockFlowMapper.insertFlow(
                    item.getProductId(),
                    item.getQuantity(),
                    "CANCEL",
                    order.getOrderNo()
            );
        }
        // 操作日志
        OpLog log = new OpLog();
        log.setAdminUserId(adminUserId);
        log.setAction("取消订单");
        log.setTarget("order:" + orderId);
        opLogMapper.insert(log);
    }
    // 更新订单的收货地址快照
    @Override
    @Transactional
    public void updateOrderAddress(Long orderId, String addressJson, Long adminUserId) {
        orderMapper.updateAddressSnapshot(orderId, addressJson);
        // 记录管理员操作日志
        OpLog log = new OpLog();
        log.setAdminUserId(adminUserId);
        log.setAction("修改订单地址");
        log.setTarget("order:" + orderId);
        opLogMapper.insert(log);
    }






}
