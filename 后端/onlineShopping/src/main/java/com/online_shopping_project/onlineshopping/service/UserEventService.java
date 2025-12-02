package com.online_shopping_project.onlineshopping.service;

public interface UserEventService {
    //记录用户行为
    void record(Long userId, String eventType, String contentJson);
}
