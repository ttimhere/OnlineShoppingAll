package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.OrderItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * OrderItemMapper 接口
 * 用于操作订单明细表（order_item）
 * 主要负责插入每个订单中的商品项信息。
 */
@Mapper
public interface OrderItemMapper {
    //向订单明细表中插入一条记录
    @Insert("INSERT INTO order_item(order_id, product_id, product_title, price, quantity, img) " +
            "VALUES(#{orderId}, #{productId}, #{productTitle}, #{price}, #{quantity}, #{img})")
    int insertItem(@Param("orderId") Long orderId, //订单ID
                   @Param("productId") Long productId, //商品ID
                   @Param("productTitle") String productTitle, //商品标题
                   @Param("price") BigDecimal price, //下单时的商品单价
                   @Param("quantity") Integer quantity, //购买数量
                   @Param("img") String img); //商品主图
    // 根据订单ID查询所有商品明细
    @Select("SELECT id, order_id, product_id, product_title, price, quantity, img " +
            "FROM order_item " +
            "WHERE order_id = #{orderId}")
    List<OrderItem> listByOrderId(@Param("orderId") Long orderId);
}