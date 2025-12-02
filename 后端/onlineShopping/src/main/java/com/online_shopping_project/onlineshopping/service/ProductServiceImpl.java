package com.online_shopping_project.onlineshopping.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.entity.Category;
import com.online_shopping_project.onlineshopping.entity.Product;
import com.online_shopping_project.onlineshopping.entity.ProductImage;
import com.online_shopping_project.onlineshopping.entity.dto.ProductDTO;
import com.online_shopping_project.onlineshopping.entity.query.ProductQuery;
import com.online_shopping_project.onlineshopping.mapper.CategoryMapper;
import com.online_shopping_project.onlineshopping.mapper.ProductImageMapper;
import com.online_shopping_project.onlineshopping.mapper.ProductMapper;
import com.online_shopping_project.onlineshopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.online_shopping_project.onlineshopping.mapper.CategoryMapper;
//商品服务实现类 负责实现商品相关的业务逻辑，例如分页查询商品信息、为商品添加图片等。
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;// 商品数据访问层（Mapper），负责操作商品表
    private final ProductImageMapper productImageMapper;// 商品图片数据访问层，负责操作商品图片表
    private final CategoryMapper categoryMapper; // 类目数据访问层
    //分页查询商品信息
    @Override
    public PageInfo<ProductDTO> pageProducts(int pageNum, int pageSize, ProductQuery query) {
        // 如果有分类ID，则递归查出所有子分类ID
        if (query.getCategoryId() != null) {
            List<Long> ids = new ArrayList<>();
            collectCategoryIds(query.getCategoryId(), ids);
            query.setCategoryIds(ids);
        }
        PageHelper.startPage(pageNum, pageSize);// 启用分页
        List<Product> list = productMapper.selectByQuery(query);//从数据库查询符合条件的商品列表
        // 将商品实体类Product转换为 DTO（包含图片信息）
        List<ProductDTO> dtos = list.stream().map(p -> {
            ProductDTO dto = new ProductDTO();
            // 基本字段映射
            dto.setId(p.getId());
            dto.setCategoryId(p.getCategoryId());
            dto.setTitle(p.getTitle());
            dto.setSubTitle(p.getSubTitle());
            dto.setPrice(p.getPrice());
            dto.setStock(p.getStock());
            dto.setMainImg(p.getMainImg());
            // 查询并设置商品图片集合
            List<ProductImage> images = productImageMapper.selectByProductId(p.getId());
            dto.setImages(images);
            return dto;
        }).collect(Collectors.toList());
        //构建分页结果对象（PageInfo）
        PageInfo<Product> origin = new PageInfo<>(list);
        PageInfo<ProductDTO> page = new PageInfo<>();
        // 复制分页元信息（页码、总页数、总记录数）
        page.setPageNum(origin.getPageNum());
        page.setPageSize(origin.getPageSize());
        page.setTotal(origin.getTotal());
        page.setPages(origin.getPages());
        //替换列表数据为转换后的 DTO 集合
        page.setList(dtos);
        return page;
    }

    //批量为商品添加图片
    @Override
    public void addImages(Long productId, List<String> urls) {
        if (urls == null || urls.isEmpty()) return;
        productImageMapper.insertBatch(productId, urls);
    }
    //递归收集指定分类及其所有子分类ID
    private void collectCategoryIds(Long parentId, List<Long> ids) {
        // 加入当前分类
        ids.add(parentId);
        // 查询当前分类的直接子分类
        List<Category> children = categoryMapper.selectByParentId(parentId);
        // 判空防止 NullPointerException
        if (children != null && !children.isEmpty()) {
            for (int i = 0; i < children.size(); i++) {
                Category child = children.get(i);
                collectCategoryIds(child.getId(), ids);
            }
        }
    }
    @Override
    public ProductDTO getDetail(Long productId) {
        // 根据商品 ID 查询商品基本信息
        Product p = productMapper.selectById(productId);
        if (p == null) return null; // 若商品不存在，直接返回 null
        // 创建返回给前端的 DTO，设置商品基本属性
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setCategoryId(p.getCategoryId());
        dto.setTitle(p.getTitle());
        dto.setSubTitle(p.getSubTitle());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setMainImg(p.getMainImg());
        // 查询商品图片
        List<ProductImage> images = productImageMapper.selectByProductId(productId);
        dto.setImages(images);
        return dto;
    }
}


