package com.online_shopping_project.onlineshopping.controller;
import com.online_shopping_project.onlineshopping.common.AuthContext;
import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.service.AdminReportService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin/report")
public class AdminReportController {
    private final AdminReportService adminReportService;
    //构造方法注入 AdminReportService
    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }
    //判断当前管理员是否具有访问报表模块的权限
    private boolean hasPermission(HttpServletRequest req) {
        var roles = AuthContext.getAdminRoles(req);
        return roles.contains("SUPER_ADMIN") || roles.contains("CUSTOMER_SERVICE");
    }
    //获取今日、本月销售与订单概要统计
    @GetMapping("/summary")
    public Result<Map<String, Object>> summary(HttpServletRequest request) {
        if (!hasPermission(request)) {
            return Result.error("无权限访问销售报表");
        }
        return Result.success(adminReportService.getSummary());
    }
    //获取热销商品 Top 5
    @GetMapping("/hot-top5")
    public Result<List<Map<String, Object>>> hotTop5(HttpServletRequest request) {
        if (!hasPermission(request)) {
            return Result.error("无权限访问销售报表");
        }
        return Result.success(adminReportService.hotTop5());
    }
    //按商品统计销量
    @GetMapping("/by-product")
    public Result<List<Map<String, Object>>> byProduct(HttpServletRequest request) {
        if (!hasPermission(request)) {
            return Result.error("无权限访问销售报表");
        }
        return Result.success(adminReportService.byProduct());
    }
    //按分类统计销量
    @GetMapping("/by-category")
    public Result<List<Map<String, Object>>> byCategory(HttpServletRequest request) {
        if (!hasPermission(request)) {
            return Result.error("无权限访问销售报表");
        }
        return Result.success(adminReportService.byCategory());
    }
}
