package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.entity.UserAddress;
import java.util.List;
//用户地址服务接口
public interface UserAddressService {
    // 获取用户地址列表
    List<UserAddress> getAddressList(Long userId);
    // 新增地址
    void addAddress(Long userId, UserAddress address);
    // 修改地址
    void updateAddress(Long userId, UserAddress address);
    // 删除地址
    void deleteAddress(Long userId, Long addressId);
    // 设置默认地址
    void setDefaultAddress(Long userId, Long addressId);
}
