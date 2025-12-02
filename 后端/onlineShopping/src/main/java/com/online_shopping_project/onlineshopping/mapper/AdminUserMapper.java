package com.online_shopping_project.onlineshopping.mapper;

import com.online_shopping_project.onlineshopping.entity.AdminUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AdminUserMapper {
    // 根据用户名查询管理员信息，用于后台登录校验
    @Select("""
        SELECT id, username, password_hash, nickname, status, created_at, updated_at
        FROM admin_user
        WHERE username = #{username}
        LIMIT 1
    """)
    @Results(id="AdminUserMap", value = {
            @Result(column = "id",            property = "id",            id = true),
            @Result(column = "username",      property = "username"),
            @Result(column = "password_hash", property = "passwordHash"),
            @Result(column = "nickname",      property = "nickname"),
            @Result(column = "status",        property = "status"),
            @Result(column = "created_at",    property = "createdAt"),
            @Result(column = "updated_at",    property = "updatedAt")
    })
    AdminUser findByUsername(@Param("username") String username);
    // 新增管理员记录，自动生成主键ID
    @Insert("""
        INSERT INTO admin_user(username, password_hash, nickname, status, created_at, updated_at)
        VALUES(#{username}, #{passwordHash}, #{nickname}, #{status}, NOW(), NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AdminUser user);

}
