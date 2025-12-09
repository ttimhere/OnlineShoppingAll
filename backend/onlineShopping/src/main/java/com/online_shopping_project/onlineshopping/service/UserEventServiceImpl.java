package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.mapper.UserEventWriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEventServiceImpl implements UserEventService {
    @Autowired
    private UserEventWriteMapper userEventWriteMapper;
    @Override
    public void record(Long userId, String eventType, String contentJson) {
        if (userId == null) return; // 未登录用户无需记录
        userEventWriteMapper.insertEvent(userId, eventType, contentJson);
    }
}
