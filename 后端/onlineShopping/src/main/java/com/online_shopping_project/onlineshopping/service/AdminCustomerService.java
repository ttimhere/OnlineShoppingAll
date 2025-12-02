package com.online_shopping_project.onlineshopping.service;
import com.github.pagehelper.PageInfo;
import com.online_shopping_project.onlineshopping.entity.CustomerSummaryVO;
import com.online_shopping_project.onlineshopping.entity.UserEvent;
// 后台客户管理与行为日志查询 Service
public interface AdminCustomerService {
    //分页查询客户列表 + 订单统计
    PageInfo<CustomerSummaryVO> pageCustomerSummary(String keyword, int pageNum, int pageSize);
    //分页查询 user_event 日志
    PageInfo<UserEvent> pageUserEvents(Long userId, String eventType, int pageNum, int pageSize);
}
