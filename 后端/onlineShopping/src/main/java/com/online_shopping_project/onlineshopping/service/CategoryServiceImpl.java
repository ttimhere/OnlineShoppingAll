package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.entity.Category;
import com.online_shopping_project.onlineshopping.entity.dto.CategoryNode;
import com.online_shopping_project.onlineshopping.mapper.CategoryMapper;
import com.online_shopping_project.onlineshopping.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * 类目Category服务实现类，从数据库读取类目信息，并构建成树形结构返回。
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;// 数据访问层接口，用于操作类目表
    //获取类目树结构
    @Override
    public List<CategoryNode> getCategoryTree() {
        List<Category> all = categoryMapper.selectAllActive();// 查询所有启用状态的类目
        Map<Long, CategoryNode> map = new HashMap<>();// Map 用于暂存所有节点，方便通过 ID 快速查找父节点
        List<CategoryNode> roots = new ArrayList<>();// 保存所有根节点的集合
        for (Category c : all) {   // 实体类转换为树节点对象
            CategoryNode n = new CategoryNode();
            n.setId(c.getId());
            n.setParentId(c.getParentId());
            n.setName(c.getName());
            n.setLevel(c.getLevel());
            n.setSort(c.getSort());
            map.put(n.getId(), n);// 将节点放入 map 中，便于后续组装树
        }
        for (CategoryNode n : map.values()) {   // 组装树
            if (n.getParentId() == null) {
                roots.add(n);// 没有父类目则为根节点
            } else {// 查找父节点
                CategoryNode p = map.get(n.getParentId());
                if (p != null) p.getChildren().add(n);// 父节点存在 → 加入其子节点集合
                else roots.add(n); // 父节点缺失时作为根节点处理
            }
        }
        //对每个节点的子节点进行排序
        roots.forEach(CategoryServiceImpl::sortChildrenRecursively);
        roots.sort(Comparator.comparing(CategoryNode::getSort, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(CategoryNode::getId));
        return roots;
    }
    //递归地对子节点进行排序，按 sort 字段排序（null 值放最后），若 sort 相同则按 id 排序。
    private static void sortChildrenRecursively(CategoryNode node) {
        node.getChildren().sort(Comparator
                .comparing(CategoryNode::getSort, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(CategoryNode::getId));
        node.getChildren().forEach(CategoryServiceImpl::sortChildrenRecursively);
    }
}