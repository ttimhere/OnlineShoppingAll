package com.online_shopping_project.onlineshopping.service;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.entity.dto.ProductDTO;
import com.online_shopping_project.onlineshopping.entity.query.ProductQuery;
public interface ProductService {
    // 分页查询商品信息
    PageInfo<ProductDTO> pageProducts(int pageNum, int pageSize, ProductQuery query);
    // 批量添加商品图片
    void addImages(Long productId, java.util.List<String> urls);
    // 获取商品详情
    ProductDTO getDetail(Long productId);

}
