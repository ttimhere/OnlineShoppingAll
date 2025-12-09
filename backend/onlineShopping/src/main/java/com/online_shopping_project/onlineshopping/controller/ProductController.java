package com.online_shopping_project.onlineshopping.controller;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.common.Result;
import com.online_shopping_project.onlineshopping.entity.dto.ProductDTO;
import com.online_shopping_project.onlineshopping.entity.query.ProductQuery;
import com.online_shopping_project.onlineshopping.service.ProductService;
import com.online_shopping_project.onlineshopping.service.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//商品控制，负责处理与商品相关的 HTTP 请求，例如分页查询商品、添加商品图片等。
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;// 商品服务层对象，用于处理具体的业务逻辑
    private final UserEventService userEventService; //用户行为记录对象
    @GetMapping
    public Result<PageInfo<ProductDTO>> page(
            @RequestParam(defaultValue = "1") int pageNum, //当前页码，默认值为 1
            @RequestParam(defaultValue = "10") int pageSize, //每页大小，默认值为 10
            @RequestParam(required = false) Long categoryId, //可选参数：按分类 ID 过滤
            @RequestParam(required = false) String keyword, //可选参数：关键字搜索
            @RequestParam(required = false, defaultValue = "time_desc") String orderBy //排序规则，默认按时间倒序 (time_desc)
    ) {// 构造查询对象封装查询条件
        ProductQuery q = new ProductQuery();
        q.setCategoryId(categoryId);
        q.setKeyword(keyword);
        q.setOrderBy(orderBy);
        // 调用服务层分页查询方法，并包装为标准返回结果
        return Result.success(productService.pageProducts(pageNum, pageSize, q));
    }
    @GetMapping("/{id}")
    public Result<ProductDTO> detail(@PathVariable("id") Long id,
                                     javax.servlet.http.HttpServletRequest request) {
        // 获取 userId（未登录则为 null）
        Long userId = com.online_shopping_project.onlineshopping.common.AuthContext.getUserId(request);
        // 用户行为日志（浏览商品）
        if (userId != null) {
            userEventService.record(
                    userId,
                    "VIEW_PRODUCT",
                    "{\"productId\": " + id + "}"
            );
        }
        // 使用服务层查看商品详情
        ProductDTO dto = productService.getDetail(id);
        return Result.success(dto);
    }
    @PostMapping("/{id}/images")
    public Result<Void> addImages(@PathVariable("id") Long productId, //商品 ID
                                  @RequestBody List<String> urls) { //图片 URL 列表
        productService.addImages(productId, urls); // 调用服务层方法为商品添加图片
        return Result.success();
    }
}

