package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.entity.UserAddress;
import com.online_shopping_project.onlineshopping.mapper.UserAddressMapper;
import com.online_shopping_project.onlineshopping.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
//用户地址服务实现
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    // 注入地址相关的 Mapper
    private final UserAddressMapper userAddressMapper;
    // 根据用户 ID 查询地址列表
    @Override
    public List<UserAddress> getAddressList(Long userId) {
        return userAddressMapper.selectByUserId(userId);
    }
    //新增收货地址
    @Override
    public void addAddress(Long userId, UserAddress address) {
        address.setUserId(userId);
        userAddressMapper.insert(address);
    }
    //修改收货地址
    @Override
    public void updateAddress(Long userId, UserAddress address) {
        address.setUserId(userId);
        userAddressMapper.update(address);
    }
    //删除收货地址
    @Override
    public void deleteAddress(Long userId, Long addressId) {
        userAddressMapper.delete(addressId, userId);
    }
    //设置默认收货地址
    @Override
    public void setDefaultAddress(Long userId, Long addressId) {
        // 先重置所有地址的默认状态
        userAddressMapper.resetDefault(userId);
        // 设置指定地址为默认地址
        userAddressMapper.setDefault(addressId, userId);
    }
}
