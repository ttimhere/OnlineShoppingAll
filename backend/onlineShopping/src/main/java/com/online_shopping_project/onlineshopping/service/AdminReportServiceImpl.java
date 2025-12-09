package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.mapper.AdminReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//后台统计报表服务实现类
@Service
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {
    // Mapper 层，用于执行各种统计类 SQL 查询
    private final AdminReportMapper adminReportMapper;
    //查询统计概要信息，包括日销售额、本月销售额、今日订单数、本月订单数
    //返回 Map<String, Object>，前端可直接用于展示数据卡片。
    @Override
    public Map<String, Object> getSummary() {
        Map<String, Object> m = new HashMap<>();
        m.put("今日销售额", adminReportMapper.todaySales());
        m.put("本月销售额", adminReportMapper.monthSales());
        m.put("今日订单数", adminReportMapper.todayOrderCount());
        m.put("本月订单数", adminReportMapper.monthOrderCount());
        return m;
    }
    //查询热销商品 TOP 5。
    //按销量（quantity）倒序排序，返回前 5 个商品。
    @Override
    public List<Map<String, Object>> hotTop5() {
        return adminReportMapper.hotProductsTop5();
    }
    //按商品统计整体销量情况
    @Override
    public List<Map<String, Object>> byProduct() {
        return adminReportMapper.salesByProduct();
    }
    //按商品分类统计销量
    @Override
    public List<Map<String, Object>> byCategory() {
        return adminReportMapper.salesByCategory();
    }
}
