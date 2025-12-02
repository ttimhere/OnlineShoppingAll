package com.online_shopping_project.onlineshopping.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.entity.CustomerSummaryVO;
import com.online_shopping_project.onlineshopping.entity.UserEvent;
import com.online_shopping_project.onlineshopping.mapper.AdminCustomerMapper;
import com.online_shopping_project.onlineshopping.mapper.AdminUserEventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
//后台客户管理与行为日志查询实现
@Service
public class AdminCustomerServiceImpl implements AdminCustomerService {
    @Autowired
    private AdminCustomerMapper adminCustomerMapper; // 客户相关数据查询
    @Autowired
    private AdminUserEventMapper adminUserEventMapper; // 用户行为日志查询
    @Override
    public PageInfo<CustomerSummaryVO> pageCustomerSummary(String keyword, int pageNum, int pageSize) {
        // 启动分页
        PageHelper.startPage(pageNum, pageSize);
        // 查询客户总览列表
        List<CustomerSummaryVO> list = adminCustomerMapper.listCustomerSummary(keyword);
        // 封装成 PageInfo 返回
        return new PageInfo<>(list);
    }
    @Override
    public PageInfo<UserEvent> pageUserEvents(Long userId, String eventType, int pageNum, int pageSize) {
        // 启动分页
        PageHelper.startPage(pageNum, pageSize);
        // 根据用户与事件类型查询行为记录
        List<UserEvent> list = adminUserEventMapper.queryUserEvents(userId, eventType);
        // 封装成分页结果
        return new PageInfo<>(list);
    }
}
