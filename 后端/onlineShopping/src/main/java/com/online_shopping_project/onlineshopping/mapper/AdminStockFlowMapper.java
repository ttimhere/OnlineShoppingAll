package com.online_shopping_project.onlineshopping.mapper;
import org.apache.ibatis.annotations.*;
//商品库存管理相关操作
@Mapper
public interface AdminStockFlowMapper {
    // 扣减库存
    @Update("""
        UPDATE product
        SET stock = stock - #{delta}
        WHERE id = #{productId}
          AND stock >= #{delta}  -- 防止出现库存不足时库存被扣成负数-->
        """)
    int decreaseStock(@Param("productId") Long productId, //商品 ID
                      @Param("delta") Integer delta); // 扣减数量

    //用于消订单取恢复库存
    @Update("""
        UPDATE product
        SET stock = stock + #{delta}
        WHERE id = #{productId}
        """)
    int increaseStock(@Param("productId") Long productId,//商品 ID
                      @Param("delta") Integer delta);// 恢复数量


    // 写库存流水 stock_flow 记录所有库存变动
    @Insert("""
        INSERT INTO stock_flow(product_id, delta, type, ref_no, created_at)
        VALUES(#{productId}, #{delta}, #{type}, #{refNo}, NOW())
        """)
    int insertFlow(@Param("productId") Long productId, //商品 ID
                   @Param("delta") Integer delta, //本次增减数量
                   @Param("type") String type, //操作类型
                   @Param("refNo") String refNo); //关联业务编号
}
