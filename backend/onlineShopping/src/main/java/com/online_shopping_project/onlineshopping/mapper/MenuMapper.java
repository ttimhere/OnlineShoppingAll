package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.Menu;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MenuMapper {
    // 查询管理员可访问的菜单权限
    @Select("""
        SELECT 
            m.id,
            m.name,
            m.path,
            m.permission,
            m.parent_id
        FROM menu m
        JOIN role_menu rm ON rm.menu_id = m.id
        JOIN admin_user_role ar ON ar.role_id = rm.role_id
        WHERE ar.admin_user_id = #{adminUserId}
        ORDER BY m.id ASC
    """)
    @Results(id = "MenuMap", value = {
            @Result(column = "id",         property = "id", id = true),
            @Result(column = "name",       property = "name"),
            @Result(column = "path",       property = "path"),
            @Result(column = "permission", property = "permission"),
            @Result(column = "parent_id",  property = "parentId")
    })
    List<Menu> selectMenusByAdminId(Long adminUserId);
}
