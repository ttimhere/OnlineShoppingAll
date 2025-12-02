package com.online_shopping_project.onlineshopping.mapper;

import com.online_shopping_project.onlineshopping.entity.CartItem;
import com.online_shopping_project.onlineshopping.entity.CartItemVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartItemMapper {
    CartItem selectByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);// 根据用户ID和商品ID查询购物车项
    int insert(CartItem item);  // 插入新的购物车项
    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);  // 更新购物车中商品数量
    int updateSelected(@Param("id") Long id, @Param("selected") Boolean selected);  // 修改购物车项选中状态
    int deleteByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);// 删除指定用户的指定商品
    List<CartItemVO> selectCartDetail(@Param("userId") Long userId); // 查询购物车详细列表
    int deleteSelectedByUser(@Param("userId") Long userId);  // 删除选中项
}