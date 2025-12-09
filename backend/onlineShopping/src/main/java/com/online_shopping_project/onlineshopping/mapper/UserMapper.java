package com.online_shopping_project.onlineshopping.mapper;

import com.online_shopping_project.onlineshopping.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
/**
 * 用户数据访问层（MyBatis映射接口）
 * 负责执行数据库中的注册与查询操作。
 */
@Mapper
public interface UserMapper {
    //根据邮箱查询用户信息
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(@Param("email") String email);
    //插入新用户记录
    @Insert("INSERT INTO user(email, phone, password_hash, nickname, status, created_at, updated_at) " +
            "VALUES(#{email}, #{phone}, #{passwordHash}, #{nickname}, #{status}, NOW(), NOW())")
    int insert(User user);
    // 根据ID查询用户
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Long id);
}

