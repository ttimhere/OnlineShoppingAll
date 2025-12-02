package com.online_shopping_project.onlineshopping.service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AdminReportService {

    Map<String, Object> getSummary();   //今日和本月销售额与订单量
    List<Map<String, Object>> hotTop5();  //热销商品前五
    List<Map<String, Object>> byProduct();   //按商品统计销量
    List<Map<String, Object>> byCategory();   //按分类统计销量
}
