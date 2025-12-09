package com.online_shopping_project.onlineshopping.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_shopping_project.onlineshopping.entity.UserAddress;
import com.online_shopping_project.onlineshopping.entity.dto.CartAddDTO;
import com.online_shopping_project.onlineshopping.entity.dto.CartUpdateDTO;
import com.online_shopping_project.onlineshopping.entity.CartItem;
import com.online_shopping_project.onlineshopping.mapper.*;
import com.online_shopping_project.onlineshopping.service.CartService;
import com.online_shopping_project.onlineshopping.entity.CartItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
//CartServiceImpl 实现类，负责购物车的核心业务逻辑
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    //依赖的 Mapper 层
    private final CartItemMapper cartItemMapper;// 购物车表操作
    private final OrderMapper orderMapper;  // 订单主表操作
    private final OrderItemMapper orderItemMapper; // 订单明细操作
    private final ProductMapperExt productMapperExt; // 商品库存操作
    private final PaymentMapper paymentMapper;  // 支付记录操作
    private final UserEventService userEventService;//注入用户事件记录服务层
    private final UserAddressMapper userAddressMapper;
    //邮件发送相关
    private final JavaMailSender mailSender;  // 邮件发送器
    private final org.springframework.core.env.Environment env;// 环境变量配置，用于读取发件人邮箱
    @Override
    @Transactional
    public void add(CartAddDTO dto, Long userId) { // 添加商品到购物车
        // 查询购物车中是否已有该商品
        CartItem exist = cartItemMapper.selectByUserAndProduct(userId, dto.getProductId());
        if (exist == null) {// 不存在则新建一条记录
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(dto.getProductId());
            item.setQuantity(dto.getQuantity());
            item.setSelected(true); // 默认选中
            cartItemMapper.insert(item);
        } else {// 已存在则叠加数量
            int newQty = Math.max(1, exist.getQuantity() + dto.getQuantity());
            cartItemMapper.updateQuantity(exist.getId(), newQty);
        }
    }
    @Override
    public List<CartItemVO> list(Long userId) {// 获取用户购物车明细（含商品信息）
        return cartItemMapper.selectCartDetail(userId);
    }
    @Override
    @Transactional
    public void update(CartUpdateDTO dto, Long userId) {// 更新购物车条目（数量或选中状态）
        CartItem exist = cartItemMapper.selectByUserAndProduct(userId, dto.getProductId());
        if (exist == null) return;
        if (dto.getQuantity() != null) {  // 更新数量
            if (dto.getQuantity() <= 0) {  // 数量小于等于 0 则删除该项
                cartItemMapper.deleteByUserAndProduct(userId, dto.getProductId());
                return;
            }
            cartItemMapper.updateQuantity(exist.getId(), dto.getQuantity());
        }
        if (dto.getSelected() != null) { // 更新选中状态
            cartItemMapper.updateSelected(exist.getId(), dto.getSelected());
        }
    }
    @Override
    @Transactional
    public void delete(Long productId, Long userId) {  // 删除购物车中指定商品
        cartItemMapper.deleteByUserAndProduct(userId, productId);
    }


    @Override
    @Transactional
    public String checkout(Long userId, String emailToNotify, String addressJson){  //结算校验
        List<CartItemVO> items = cartItemMapper.selectCartDetail(userId);// 查询购物车详情
        if (CollectionUtils.isEmpty(items)) {
            throw new RuntimeException("购物车为空");}


        BigDecimal total = BigDecimal.ZERO;  // 仅统计选中项的总金额
        for (CartItemVO vo : items) {
            if (Boolean.TRUE.equals(vo.getSelected())) {
                total = total.add(vo.getPrice().multiply(new BigDecimal(vo.getQuantity())));}
        }
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("没有勾选任何商品");}
        // 生成唯一订单号（时间戳 + UUID）
        String orderNo = "O" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0,8);


        // 写入订单主表和用户行为日志
        orderMapper.insertOrder(orderNo, userId, total, total, 0, "MOCK");
        Long orderId = orderMapper.selectIdByOrderNo(orderNo);

        // 写订单地址
        if (addressJson != null && !addressJson.isEmpty()) {
            orderMapper.updateAddressSnapshot(orderId, addressJson);
        }


        userEventService.record(
                userId,
                "CREATE_ORDER",
                "{\"orderId\": " + orderId + ",\"amount\": " + total + "}");
        for (CartItemVO vo : items) {   // 扣减库存并写入订单明细表
            if (!Boolean.TRUE.equals(vo.getSelected())) continue;
            // 扣库存（如果失败则抛出异常）
            int ok = productMapperExt.decreaseStock(vo.getProductId(), vo.getQuantity());
            if (ok <= 0) {
                throw new RuntimeException("库存不足：" + vo.getTitle());}
            orderItemMapper.insertItem(orderId, vo.getProductId(), vo.getTitle(), vo.getPrice(), // 写入订单项信息
                    vo.getQuantity(), vo.getImg());}
        // 模拟支付成功，记录支付流水并更新订单状态 写入用户行为日志
        String requestNo = "P" + System.currentTimeMillis();
        paymentMapper.insertPayment(orderNo, "MOCK", requestNo, total, 1);
        orderMapper.markPaid(orderNo);
        userEventService.record(
                userId,
                "PAY_SUCCESS",
                "{\"orderId\": " + orderId + "}"
        );
        cartItemMapper.deleteSelectedByUser(userId);// 删除购物车中已选中的条目
        sendPaidMail(emailToNotify, orderNo, total); // 发送邮件通知
        return orderNo;
    }
    // 发送支付成功邮件
    private void sendPaidMail(String to, String orderNo, BigDecimal amount) {
        if (to == null || to.isEmpty()) return;
        String from = env.getProperty("app.mail.from", ""); // 从配置文件中读取发件人邮箱
        // 构建邮件内容
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("订单支付成功通知");
        msg.setText("您的订单 " + orderNo + " 已支付成功，金额：" + amount + "。感谢您的购买！");
        mailSender.send(msg);
    }
}