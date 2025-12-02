package com.online_shopping_project.onlineshopping.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
/**
 * ProductMapperExt 接口
 * 用于操作商品库存相关的扩展功能。
 * 主要负责在用户下单后，安全地减少商品库存数量。
 */
@Mapper
public interface ProductMapperExt {
    /**
     * 减少商品库存（下单时调用）
     * - 从 product 表中扣减库存：`stock = stock - delta`
     * - 同时加上条件 stock >= delta，防止库存变成负数。
     * - 若库存不足，SQL 不会更新任何记录（返回值为 0）。
     * */
    @Update("UPDATE product " +
            "SET stock = stock - #{delta} " +
            "WHERE id = #{productId} AND stock >= #{delta}")
    int decreaseStock(@Param("productId") Long productId, //商品ID
                      @Param("delta") Integer delta); //扣减数量（购买的数量）
}