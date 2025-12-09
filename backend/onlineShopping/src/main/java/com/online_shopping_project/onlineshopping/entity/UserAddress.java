package com.online_shopping_project.onlineshopping.entity;
import lombok.Data;
import java.io.Serializable;
/**
 * 用户收货地址实体类
 */
@Data
public class UserAddress implements Serializable {
    private Long id;   // 地址ID
    private Long userId;    // 用户ID
    private String receiver;  // 收货人姓名
    private String phone;   // 收货人电话
    private String province;   // 省份
    private String city;   // 城市
    private String district;   // 区域
    private String detail;  // 详细地址
    private Boolean isDefault;  // 是否为默认地址（0：否，1：是）
}
