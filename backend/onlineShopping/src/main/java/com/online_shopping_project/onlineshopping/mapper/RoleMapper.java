package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.Role;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RoleMapper {
    // 根据管理员ID查询其角色列表，构建后台权限模型
    @Select("""
        SELECT
            r.id,
            r.name,
            r.description
        FROM role r
        JOIN admin_user_role ar ON ar.role_id = r.id
        WHERE ar.admin_user_id = #{adminUserId}
    """)
    @Results(id = "RoleMap", value = {
            @Result(column = "id",          property = "id", id = true),
            @Result(column = "name",        property = "name"),
            @Result(column = "description", property = "description")
    })
    List<Role> selectRolesByAdminId(Long adminUserId);

}