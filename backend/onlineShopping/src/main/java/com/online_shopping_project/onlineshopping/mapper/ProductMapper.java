package com.online_shopping_project.onlineshopping.mapper;

import com.online_shopping_project.onlineshopping.entity.Product;
import com.online_shopping_project.onlineshopping.entity.query.ProductQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    // 根据查询条件筛选商品信息
    List<Product> selectByQuery(@Param("q") ProductQuery q);
    // 后台管理专用方法
    // 后台条件查询商品
    List<Product> selectByAdminQuery(@Param("title") String title,
                                     @Param("categoryId") Long categoryId,
                                     @Param("status") Integer status);
    // 后台按 ID 查询商品
    Product selectById(@Param("id") Long id);
    // 后台新增商品
    int insert(Product product);
    // 后台编辑商品
    int update(Product product);
    // 后台修改商品状态（上架 / 下架）
    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status);

}
