package com.online_shopping_project.onlineshopping.mapper;
import com.online_shopping_project.onlineshopping.entity.UserAddress;
import org.apache.ibatis.annotations.*;
import java.util.List;
//用户地址数据访问层
@Mapper
public interface UserAddressMapper {
    // 查询用户所有地址
    @Select("SELECT * FROM user_address WHERE user_id = #{userId}")
    List<UserAddress> selectByUserId(@Param("userId") Long userId);
    // 新增用户地址
    @Insert("INSERT INTO user_address(user_id, receiver, phone, province, city, district, detail, is_default) " +
            "VALUES(#{userId}, #{receiver}, #{phone}, #{province}, #{city}, #{district}, #{detail}, #{isDefault})")
    int insert(UserAddress address);
    // 更新用户地址
    @Update("UPDATE user_address SET receiver = #{receiver}, phone = #{phone}, province = #{province}, " +
            "city = #{city}, district = #{district}, detail = #{detail}, is_default = #{isDefault} " +
            "WHERE id = #{id} AND user_id = #{userId}")
    int update(UserAddress address);
    // 删除用户地址
    @Delete("DELETE FROM user_address WHERE id = #{id} AND user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId);
    // 设置默认地址
    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    int resetDefault(@Param("userId") Long userId);
    // 设置某个地址为默认
    @Update("UPDATE user_address SET is_default = 1 WHERE id = #{id} AND user_id = #{userId}")
    int setDefault(@Param("id") Long id, @Param("userId") Long userId);
}
