package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.Product;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
//后台统计报表相关查询
@Mapper
public interface AdminReportMapper {
    // 今日销售额统计
    //COALESCE 防止 SUM 结果为 null（无订单时为 0），仅统计 pay_time 为今天的订单
    @Select("""
        SELECT COALESCE(SUM(pay_amount), 0)
        FROM `order`
        WHERE status IN (1,2,3)
          AND DATE(pay_time) = CURDATE()
    """)
    BigDecimal todaySales();

    // 本月销售额
    //使用 DATE_FORMAT(pay_time,'%Y-%m') 进行年月匹配，只统计有效订单
    @Select("""
        SELECT COALESCE(SUM(pay_amount), 0)
        FROM `order`
        WHERE status IN (1,2,3)
          AND DATE_FORMAT(pay_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m')
    """)
    BigDecimal monthSales();

    // 今日订单数
    @Select("""
        SELECT COUNT(*)
        FROM `order`
        WHERE status IN (1,2,3)
          AND DATE(pay_time) = CURDATE()
    """)
    Integer todayOrderCount();

    // 本月订单数
    @Select("""
        SELECT COUNT(*)
        FROM `order`
        WHERE status IN (1,2,3)
          AND DATE_FORMAT(pay_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m')
    """)
    Integer monthOrderCount();

    // 热销商品 TOP 5：查询销量最多的前 5 个商品
    @Select("""
        SELECT 
            oi.product_id AS productId,
            oi.product_title AS title,
            SUM(oi.quantity) AS totalQuantity -- 销量数量 
        FROM order_item oi
        JOIN `order` o ON oi.order_id = o.id
        WHERE o.status IN (1,2,3)
        GROUP BY oi.product_id, oi.product_title
        ORDER BY totalQuantity DESC
        LIMIT 5
    """)
    List<Map<String, Object>> hotProductsTop5();

    // 按商品统计销售数据
    @Select("""
        SELECT 
            oi.product_id AS productId,
            oi.product_title AS title,
            SUM(oi.quantity) AS totalQuantity, -- 销量数量 
            SUM(oi.price * oi.quantity) AS totalAmount  -- 销售额 
        FROM order_item oi
        JOIN `order` o ON oi.order_id = o.id
        WHERE o.status IN (1,2,3)
        GROUP BY oi.product_id, oi.product_title
        ORDER BY totalQuantity DESC
    """)
    List<Map<String, Object>> salesByProduct();

    // 按分类统计销量
    @Select("""
        SELECT 
            c.id AS categoryId,
            c.name AS categoryName,
            SUM(oi.quantity) AS totalQuantity, -- 销量数量 
            SUM(oi.price * oi.quantity) AS totalAmount -- 销售额 
        FROM order_item oi
        JOIN product p ON oi.product_id = p.id
        JOIN category c ON p.category_id = c.id
        JOIN `order` o ON oi.order_id = o.id
        WHERE o.status IN (1,2,3)
        GROUP BY c.id, c.name
        ORDER BY totalQuantity DESC
    """)
    List<Map<String, Object>> salesByCategory();
}
