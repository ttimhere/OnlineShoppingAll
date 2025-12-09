package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.OrderItem;
import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface AdminOrderItemMapper {
    // OrderItem 字段映射
    @Results(id = "AdminOrderItemMap", value = {
            @Result(column = "id",               property = "id", id = true),
            @Result(column = "order_id",         property = "orderId"),
            @Result(column = "product_id",       property = "productId"),
            @Result(column = "product_title",    property = "productTitle"),
            @Result(column = "price",            property = "price"),
            @Result(column = "quantity",         property = "quantity"),
            @Result(column = "img",              property = "img")
    })
    //根据订单 ID 查询该订单的所有商品项
    @Select("""
        SELECT *
        FROM order_item
        WHERE order_id = #{orderId}
    """)
    List<OrderItem> selectByOrderId(Long orderId);
}
