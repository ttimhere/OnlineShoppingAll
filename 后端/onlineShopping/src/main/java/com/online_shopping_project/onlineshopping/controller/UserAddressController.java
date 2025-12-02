package com.online_shopping_project.onlineshopping.controller;
import com.online_shopping_project.onlineshopping.common.AuthContext;
import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.entity.UserAddress;
import com.online_shopping_project.onlineshopping.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
//用户地址管理控制器
@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;
    // 获取用户所有地址
    @GetMapping("/list")
    public Result<List<UserAddress>> list(HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);

        List<UserAddress> addressList = userAddressService.getAddressList(userId);
        return Result.success(addressList);
    }
    // 新增用户地址
    @PostMapping("/add")
    public Result<Void> add(@RequestBody UserAddress address, HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        userAddressService.addAddress(userId, address);
        return Result.success();
    }
    // 修改用户地址
    @PostMapping("/update")
    public Result<Void> update(@RequestBody UserAddress address, HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        userAddressService.updateAddress(userId, address);
        return Result.success();
    }
    // 删除用户地址
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        userAddressService.deleteAddress(userId, id);
        return Result.success();
    }
    // 设置默认地址
    @PostMapping("/setDefault/{id}")
    public Result<Void> setDefault(@PathVariable Long id, HttpServletRequest request) {
        Long userId = AuthContext.getUserId(request);
        userAddressService.setDefaultAddress(userId, id);
        return Result.success();
    }
}
