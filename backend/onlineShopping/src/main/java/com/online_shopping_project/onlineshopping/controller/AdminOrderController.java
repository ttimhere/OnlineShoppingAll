package com.online_shopping_project.onlineshopping.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.common.AuthContext;
import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.entity.Order;
import com.online_shopping_project.onlineshopping.entity.OrderDetailVO;
import com.online_shopping_project.onlineshopping.entity.UserAddress;
import com.online_shopping_project.onlineshopping.service.AdminOrderService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderController {
    //通过构造方法注入 AdminOrderService，
    private final AdminOrderService adminOrderService;
    private final AdminOrderService orderService;
    public AdminOrderController(AdminOrderService adminOrderService, AdminOrderService orderService) {
        this.adminOrderService = adminOrderService;
        this.orderService = orderService;
    }
    //判断当前管理员是否具有订单管理权限
    private boolean hasOrderPermission(HttpServletRequest request) {
        List<String> roles = AuthContext.getAdminRoles(request);
        return roles.contains("SUPER_ADMIN") || roles.contains("CUSTOMER_SERVICE");
    }
    //订单分页查询
    @GetMapping("/list")
    public Result<PageInfo<Order>> list(HttpServletRequest request,
                                        @RequestParam(required = false) Integer status, //订单状态
                                        @RequestParam(required = false) Long userId, //用户ID
                                        @RequestParam(required = false) String orderNo, //订单编号
                                        @RequestParam(required = false) String productTitle, //商品标题关键字
                                        @RequestParam(defaultValue = "1") int pageNum,
                                        @RequestParam(defaultValue = "10") int pageSize) {
        // 权限校验：没有权限则直接返回错误
        if (!hasOrderPermission(request)) {
            return Result.error("无权限访问订单管理");
        }
        // 调用 Service 层分页查询订单
        PageInfo<Order> page = adminOrderService.pageOrders(
                status, userId, orderNo, productTitle, pageNum, pageSize
        );
        return Result.success(page);
    }
    //查询订单详情
    @GetMapping("/{id}")
    public Result<OrderDetailVO> detail(HttpServletRequest request,
                                        @PathVariable("id") Long orderId) {
        if (!hasOrderPermission(request)) {
            return Result.error("无权限访问订单详情");
        }
        // 查询完整订单详情 VO
        OrderDetailVO vo = adminOrderService.getOrderDetail(orderId);
        return Result.success(vo);
    }
    //订单发货
    @PostMapping("/{id}/ship")
    public Result<Void> ship(HttpServletRequest request,
                             @PathVariable("id") Long orderId) {
        if (!hasOrderPermission(request)) {
            return Result.error("无权限发货");
        }
        Long adminId = AuthContext.getAdminId(request);
        // 执行发货业务逻辑
        adminOrderService.shipOrder(orderId, adminId);
        return Result.success();
    }
    //取消订单
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(HttpServletRequest request,
                               @PathVariable("id") Long orderId) {
        if (!hasOrderPermission(request)) {
            return Result.error("无权限取消订单");
        }
        Long adminId = AuthContext.getAdminId(request);
        // 执行取消订单逻辑
        adminOrderService.cancelOrder(orderId, adminId);
        return Result.success();
    }
    //更改订单地址
    @PutMapping("/{id}/address")
    public Result<Void> updateAddress(@PathVariable Long id,
                                      @RequestBody UserAddress address,
                                      HttpServletRequest request) {
        //获取管理员id
        Long adminId = AuthContext.getAdminId(request);
        // 转 JSON 字符串
        String addressJson;
        try {
            addressJson = new ObjectMapper().writeValueAsString(address);
        } catch (Exception e) {
            throw new RuntimeException("地址序列化失败");
        }
        adminOrderService.updateOrderAddress(id, addressJson, adminId);

        return Result.success();
    }
}
