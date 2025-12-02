package com.online_shopping_project.onlineshopping.controller;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.entity.CustomerSummaryVO;
import com.online_shopping_project.onlineshopping.entity.UserEvent;
import com.online_shopping_project.onlineshopping.service.AdminCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//后台客户管理 + 用户行为日志查询
@RestController
@RequestMapping("/api/admin/customer")
@Tag(name = "后台-客户管理与行为日志")
public class AdminCustomerController {
    @Autowired
    private AdminCustomerService adminCustomerService;
    //客户列表和基本统计（订单数、总消费）
    @GetMapping("/list")
    @Operation(summary = "客户列表 + 订单统计")
    public Result<PageInfo<CustomerSummaryVO>> listCustomers(
            @RequestParam(value = "keyword", required = false) String keyword, // 搜索关键字
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,   // 页码
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize // 每页数量
    ) {
        // 调用 Service 层，执行分页查询
        PageInfo<CustomerSummaryVO> pageInfo =
                adminCustomerService.pageCustomerSummary(keyword, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    //user_event 简单查询接口 查看用户浏览 / 购买行为日志
    @GetMapping("/events")
    @Operation(summary = "用户行为日志查询")
    public Result<PageInfo<UserEvent>> listUserEvents(
            @RequestParam(value = "userId", required = false) Long userId,  // 用户 ID
            @RequestParam(value = "eventType", required = false) String eventType, // 行为类型
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, // 页码
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize  // 每页数量
    ) {
        // 调用 Service 层执行查询
        PageInfo<UserEvent> pageInfo =
                adminCustomerService.pageUserEvents(userId, eventType, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
