package com.online_shopping_project.onlineshopping.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.entity.*;
import com.online_shopping_project.onlineshopping.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired private AdminUserMapper adminUserMapper;
    @Autowired private RoleMapper roleMapper;
    @Autowired private MenuMapper menuMapper;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private ProductMapper productMapper;
    @Autowired private ProductImageMapper productImageMapper;
    @Autowired private OpLogMapper opLogMapper;
    // 管理员登录
    @Override
    public AdminUser login(String username, String password) {
        AdminUser db = adminUserMapper.findByUsername(username);
        // 用户不存在 或 已禁用（status=0）
        if (db == null || db.getStatus() == 0) return null;
        // 校验密码
        return passwordEncoder.matches(password, db.getPasswordHash()) ? db : null;
    }
    // 获取管理员拥有角色
    @Override
    public List<String> getRoleCodes(Long adminUserId) {
        List<Role> roles = roleMapper.selectRolesByAdminId(adminUserId);
        return roles.stream()
                .map(Role::getName)    // name 字段表示角色编码
                .collect(Collectors.toList());
    }
    // 获取权限字符串列表
    @Override
    public List<String> getPermissions(Long adminUserId) {
        List<Menu> menus = menuMapper.selectMenusByAdminId(adminUserId);
        return menus.stream()
                .map(Menu::getPermission)
                .collect(Collectors.toList());
    }
    // 获取管理员可见菜单
    @Override
    public List<Object> getMenus(Long adminUserId) {
        return menuMapper //返回格式保持为 map 列表（方便前端使用）
                .selectMenusByAdminId(adminUserId)
                .stream()
                .map(m -> {
                    var obj = new java.util.HashMap<String, Object>();
                    obj.put("id", m.getId());
                    obj.put("name", m.getName());
                    obj.put("path", m.getPath());
                    obj.put("permission", m.getPermission());
                    obj.put("parentId", m.getParentId());
                    return obj;
                })
                .collect(Collectors.toList());
    }
    // 分页查询商品（后台管理使用）
    @Override
    public PageInfo<Product> pageProducts(int pageNum,
                                          int pageSize,
                                          String title,
                                          Long categoryId,
                                          Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectByAdminQuery(title, categoryId, status);
        return new PageInfo<>(list);
    }
    //新增商品
    @Override
    public void createProduct(Product product, List<String> images, Long adminId) {
        if (product.getStatus() == null) product.setStatus(1);
        if (product.getStock() == null) product.setStock(0);
        // 插入商品
        productMapper.insert(product);
        // 插入图片
        if (images != null && !images.isEmpty()) {
            productImageMapper.insertBatch(product.getId(), images);
        }
        // 操作日志
        OpLog log = new OpLog();
        log.setAdminUserId(adminId);
        log.setAction("新增商品");
        log.setTarget("product:" + product.getId());
        opLogMapper.insert(log);
    }
    //编辑商品
    @Override
    public void updateProduct(Product product, List<String> images, Long adminId) {
        Product db = productMapper.selectById(product.getId());
        // 校验商品是否存在
        if (db == null) throw new RuntimeException("商品不存在");
        // 更新商品信息
        productMapper.update(product);
        // 如果需要重置图片
        if (images != null && !images.isEmpty()) {
            productImageMapper.insertBatch(product.getId(), images);
        }
        // 操作日志
        OpLog log = new OpLog();
        log.setAdminUserId(adminId);
        log.setAction("编辑商品");
        log.setTarget("product:" + product.getId());
        opLogMapper.insert(log);
    }
    //上架 / 下架 商品
    @Override
    public void changeProductStatus(Long productId, Integer status, Long adminId) {
        // 更新状态（status=1 上架，0 下架）
        productMapper.updateStatus(productId, status);
        // 操作日志
        OpLog log = new OpLog();
        log.setAdminUserId(adminId);
        log.setAction(status == 1 ? "上架商品" : "下架商品");
        log.setTarget("product:" + productId);
        opLogMapper.insert(log);
    }
}
