package com.online_shopping_project.onlineshopping.mapper;

import com.online_shopping_project.onlineshopping.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductImageMapper {
    // 根据商品ID查询对应图片列表
    List<ProductImage> selectByProductId(@Param("productId") Long productId);
    // 批量插入图片数据
    int insertBatch(@Param("productId") Long productId, @Param("urls") List<String> urls);
}

