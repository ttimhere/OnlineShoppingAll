package com.online_shopping_project.onlineshopping.controller;
import com.online_shopping_project.onlineshopping.common.AuthContext;
import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.entity.*;
import com.online_shopping_project.onlineshopping.service.OrderService;
import com.online_shopping_project.onlineshopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.online_shopping_project.onlineshopping.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//用户控制层 实现注册、登录、 查询个人信息、查询订单列表、查询订单详情
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;// 用户业务逻辑处理
    @Autowired
    private OrderService orderService;// 用户订单业务处理
    @Value("${jwt.secret}")
    private String jwtSecret;// JWT 配置
    @Value("${jwt.expire-minutes}")
    private long expireMinutes;
    //用户注册接口
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        // 调用服务层注册用户
        boolean ok = userService.register(user);
        if (!ok) return Result.error("邮箱已注册");
        return Result.success("注册成功");
    }
    //用户登录接口
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        // 验证账号密码
        User dbUser = userService.login(user.getEmail(), user.getPasswordHash());
        if (dbUser == null) return Result.error("邮箱或密码错误");
        //登录成功则生成JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", String.valueOf(dbUser.getId()));
        claims.put("uname", dbUser.getNickname());
        // 生成 JWT Token
        String token = JwtUtil.generateToken( JwtUtil.getRuntimeSecret(), expireMinutes,
                dbUser.getEmail(), claims);
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("user", dbUser);
        return Result.success(resp);
    }
    //获取当前登录用户信息和历史订单摘要
    @GetMapping("/me")
    public Result<UserProfileVO> me(HttpServletRequest request) {
        try {// 从 token 中解析出用户 ID
            Long userId = AuthContext.getUserId(request);
            User user = userService.findById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            // 查询该用户的所有订单
            List<Order> orders = orderService.listOrdersByUser(userId);
            // 转换为摘要VO列表
            List<UserOrderSummaryVO> orderSummaryList = orders.stream().map(order -> {
                UserOrderSummaryVO vo = new UserOrderSummaryVO();
                vo.setId(order.getId());
                vo.setOrderNo(order.getOrderNo());
                vo.setTotalAmount(order.getTotalAmount());
                vo.setPayAmount(order.getPayAmount());
                vo.setStatus(order.getStatus());
                vo.setPayType(order.getPayType());
                vo.setCreateTime(order.getCreateTime());
                vo.setPayTime(order.getPayTime());
                vo.setShipTime(order.getShipTime());
                vo.setFinishTime(order.getFinishTime());
                vo.setCancelTime(order.getCancelTime());
                return vo;
            }).collect(Collectors.toList());
            // 组装个人中心数据
            UserProfileVO profileVO = new UserProfileVO();
            profileVO.setId(user.getId());
            profileVO.setEmail(user.getEmail());
            profileVO.setNickname(user.getNickname());
            profileVO.setOrders(orderSummaryList);
            return Result.success(profileVO);
        } catch (Exception e) {
            return Result.error("未登录");
        }
    }
    //获取当前登录用户的订单列表
    @GetMapping("/orders")
    public Result<List<UserOrderSummaryVO>> listMyOrders(HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        List<Order> orders = orderService.listOrdersByUser(userId);
        // 转为摘要 VO
        List<UserOrderSummaryVO> list = orders.stream().map(order -> {
            UserOrderSummaryVO vo = new UserOrderSummaryVO();
            vo.setId(order.getId());
            vo.setOrderNo(order.getOrderNo());
            vo.setTotalAmount(order.getTotalAmount());
            vo.setPayAmount(order.getPayAmount());
            vo.setStatus(order.getStatus());
            vo.setPayType(order.getPayType());
            vo.setCreateTime(order.getCreateTime());
            vo.setPayTime(order.getPayTime());
            vo.setShipTime(order.getShipTime());
            vo.setFinishTime(order.getFinishTime());
            vo.setCancelTime(order.getCancelTime());
            return vo;
        }).collect(Collectors.toList());

        return Result.success(list);
    }
    //获取当前登录用户查看某一订单详情
    @GetMapping("/orders/{orderId}")
    public Result<OrderDetailVO> getMyOrderDetail(@PathVariable Long orderId,
                                                  HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        OrderDetailVO detail = orderService.getOrderDetail(userId, orderId);
        if (detail == null) {
            return Result.error("订单不存在或不属于当前用户");
        }
        return Result.success(detail);
    }
    //用户确认收货
    @PostMapping("/orders/{orderId}/finish")
    public Result<Void> finishOrder(@PathVariable Long orderId,
                                    HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        orderService.finishOrder(userId, orderId);
        return Result.success();
    }
}

