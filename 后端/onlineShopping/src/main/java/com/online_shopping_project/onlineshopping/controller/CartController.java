package com.online_shopping_project.onlineshopping.controller;
import com.online_shopping_project.onlineshopping.common.AuthContext;
import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.entity.dto.CartAddDTO;
import com.online_shopping_project.onlineshopping.entity.dto.CartUpdateDTO;
import com.online_shopping_project.onlineshopping.service.CartService;
import com.online_shopping_project.onlineshopping.entity.CartItemVO;
import com.online_shopping_project.onlineshopping.service.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
//购物车Controller，提供购物车相关的 RESTful API：
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;// 注入购物车业务服务层
    private final UserEventService userEventService;//注入用户事件记录服务层
    // 添加商品到购物车
    @PostMapping("/add")
    public Result<Void> add(@RequestBody @Validated CartAddDTO dto, HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);// 从请求中提取当前登录用户 ID
        userEventService.record( // 用户行为日志：加入购物车
                userId,
                "ADD_CART",
                "{\"productId\":" + dto.getProductId() + ",\"quantity\":" + dto.getQuantity() + "}");
        cartService.add(dto, userId); // 调用业务逻辑添加商品
        return Result.success(); // 返回通用成功响应
    }
    // 查看购物车商品列表
    @GetMapping("/list")
    public Result<List<CartItemVO>> list(HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        return Result.success(cartService.list(userId));// 查询并返回用户购物车明细（带商品信息）
    }
    // 更新购物车商品（修改数量或选中状态）
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated CartUpdateDTO dto, HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        cartService.update(dto, userId);
        return Result.success();
    }
    // 删除购物车中的商品
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam("productId") Long productId, HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        cartService.delete(productId, userId);
        return Result.success();
    }
    // 结算下单并发送邮件通知
    @PostMapping("/checkout")
    public Result<String> checkout(@RequestParam(value = "email", required = false) String email,
                                   @RequestParam(value = "address", required = false) String addressJson,
                                   HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        // 如果用户未手动传 email，则使用认证上下文中的邮箱
        String userEmail = email != null ? email : AuthContext.getEmail(request);
        // 执行结算（生成订单、扣库存、模拟支付、发送邮件）
        String orderNo = cartService.checkout(userId, userEmail, addressJson);
        // 返回订单号给前端
        return Result.success(orderNo);
    }
}
