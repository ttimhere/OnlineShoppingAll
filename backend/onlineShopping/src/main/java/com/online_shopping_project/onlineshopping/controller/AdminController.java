package com.online_shopping_project.onlineshopping.controller;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.Util.JwtUtil;
import com.online_shopping_project.onlineshopping.common.AuthContext;
import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.config.SecurityConfig;
import com.online_shopping_project.onlineshopping.entity.AdminUser;
import com.online_shopping_project.onlineshopping.entity.Product;
import com.online_shopping_project.onlineshopping.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired private AdminUserService adminUserService;
    @Value("${jwt.secret}")  // 从配置文件中读取 JWT 秘钥
    private String jwtSecret;
    @Value("${jwt.expire-minutes}") // token 过期时间
    private long expireMinutes;
    private final SecurityConfig securityConfig;
    public AdminController(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }
    // 管理员登录接口
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        // 调用 service 校验密码
        AdminUser admin = adminUserService.login(username, password);
        if (admin == null) {
            return Result.error("账号或密码错误");
        }
        // // 构建 JWT payload 载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", admin.getId());  // 当前管理员 ID
        claims.put("nickname", admin.getNickname());  // 昵称用于展示
        claims.put("type", "admin");        // 用于区分用户端/管理端 token
        claims.put("roles", adminUserService.getRoleCodes(admin.getId())); // 角色
        // 生成 token
        String token = JwtUtil.generateToken(
                JwtUtil.getRuntimeSecret(),
                expireMinutes,
                admin.getUsername(),
                claims
        );
        // 返回结构
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("admin", admin);
        resp.put("roles", adminUserService.getRoleCodes(admin.getId()));
        resp.put("permissions", adminUserService.getPermissions(admin.getId()));
        resp.put("menus", adminUserService.getMenus(admin.getId()));
        return Result.success(resp);
    }

    // 商品分页查询
    @GetMapping("/products")
    public Result<PageInfo<Product>> pageProducts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int pageNum,  // 页码
            @RequestParam(defaultValue = "10") int pageSize,// 每页条数
            @RequestParam(required = false) String title,  // 按标题模糊查询
            @RequestParam(required = false) Long categoryId, // 分类筛选
            @RequestParam(required = false) Integer status  // 上下架状态
    ) {
        Long adminId = AuthContext.getAdminId(request);
        return Result.success(
                adminUserService.pageProducts(pageNum, pageSize, title, categoryId, status)
        );
    }
    // 判断是否超级管理员
    private boolean isSuperAdmin(HttpServletRequest req) {
        List<String> roles = AuthContext.getAdminRoles(req);
        return roles.contains("SUPER_ADMIN");
    }
    //  新增商品
    @PostMapping("/products")
    public Result<Void> createProduct(HttpServletRequest request,
                                      @RequestBody Map<String,Object> body) {
        // 权限判断
        if (!isSuperAdmin(request)) {
            return Result.error("只有超级管理员可以新增商品");
        }
        // 组装 Product 对象
        Product product = new Product();
        product.setCategoryId(((Number)body.get("categoryId")).longValue());
        product.setTitle((String) body.get("title"));
        product.setSubTitle((String) body.get("subTitle"));
        product.setPrice(new java.math.BigDecimal(body.get("price").toString()));
        product.setStock((Integer) body.get("stock"));
        product.setStatus((Integer) body.get("status"));
        product.setMainImg((String) body.get("mainImg"));
        product.setAttrs((String) body.get("attrs"));
        // 商品附加图
        List<String> images = (List<String>) body.get("images");
        // 调用 service 新建商品
        adminUserService.createProduct(product, images, AuthContext.getAdminId(request));
        return Result.success();
    }
    // 编辑商品
    @PutMapping("/products/{id}")
    public Result<Void> updateProduct(HttpServletRequest request,
                                      @PathVariable Long id,
                                      @RequestBody Map<String,Object> body) {
        // 超级管理员权限检查
        if (!isSuperAdmin(request)) {
            return Result.error("只有超级管理员可以编辑商品");
        }
        // 组装 product 对象
        Product product = new Product();
        product.setId(id);
        product.setCategoryId(((Number)body.get("categoryId")).longValue());
        product.setTitle((String) body.get("title"));
        product.setSubTitle((String) body.get("subTitle"));
        product.setPrice(new java.math.BigDecimal(body.get("price").toString()));
        product.setStock((Integer) body.get("stock"));
        product.setStatus((Integer) body.get("status"));
        product.setMainImg((String) body.get("mainImg"));
        product.setAttrs((String) body.get("attrs"));
        List<String> images = (List<String>) body.get("images");
        // 调用 service 进行修改
        adminUserService.updateProduct(product, images, AuthContext.getAdminId(request));
        return Result.success();
    }
    // 修改商品状态（上架/下架）
    @PatchMapping("/products/{id}/status")
    public Result<Void> changeStatus(HttpServletRequest request,
                                     @PathVariable Long id,
                                     @RequestBody Map<String,Integer> body) {
        if (!isSuperAdmin(request)) {
            return Result.error("只有超级管理员可以修改商品状态");
        }
        Integer status = body.get("status");
        adminUserService.changeProductStatus(id, status, AuthContext.getAdminId(request));
        return Result.success();
    }
}
