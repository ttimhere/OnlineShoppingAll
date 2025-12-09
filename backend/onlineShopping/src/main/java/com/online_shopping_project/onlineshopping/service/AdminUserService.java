package com.online_shopping_project.onlineshopping.service;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.entity.AdminUser;
import com.online_shopping_project.onlineshopping.entity.Product;

import java.util.List;

//后台管理员接口
public interface AdminUserService {
    //管理员登录
    AdminUser login(String username, String password);
    //查询管理员拥有的角色编码
    List<String> getRoleCodes(Long adminUserId);
    //查询管理员拥有的权限 permission 列表
    List<String> getPermissions(Long adminUserId);
    // 查询管理员可见菜单列表
    List<Object> getMenus(Long adminUserId);

    // 后台商品管理功能
    // 分页查询商品（title / categoryId / status）
    PageInfo<Product> pageProducts(int pageNum,
                                   int pageSize,
                                   String title,
                                   Long categoryId,
                                   Integer status);
    // 新增商品
    void createProduct(Product product, List<String> images, Long adminId);

    // 编辑商品
    void updateProduct(Product product, List<String> images, Long adminId);

    // 上下架商品
    void changeProductStatus(Long productId, Integer status, Long adminId);
}
