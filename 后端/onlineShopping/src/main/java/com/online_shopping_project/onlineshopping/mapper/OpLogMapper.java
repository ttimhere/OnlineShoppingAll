package com.online_shopping_project.onlineshopping.mapper;

import com.online_shopping_project.onlineshopping.entity.OpLog;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OpLogMapper {

    @Insert("""
        INSERT INTO op_log(admin_user_id, action, target, created_at)
        VALUES(#{adminUserId}, #{action}, #{target}, NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OpLog log);

}
