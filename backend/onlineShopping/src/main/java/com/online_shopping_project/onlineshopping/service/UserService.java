package com.online_shopping_project.onlineshopping.service;

import com.online_shopping_project.onlineshopping.entity.User;
/**
 * 用户服务接口
 * 定义注册与登录业务逻辑的标准方法。
 */
public interface UserService {
    boolean register(User user);  // 用户注册
    User login(String email, String password);  // 用户登录
    // 根据ID查询用户
    User findById(Long id);
}

