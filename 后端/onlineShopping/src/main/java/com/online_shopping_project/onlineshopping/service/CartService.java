package com.online_shopping_project.onlineshopping.service;

import com.online_shopping_project.onlineshopping.entity.dto.CartAddDTO;
import com.online_shopping_project.onlineshopping.entity.dto.CartUpdateDTO;
import com.online_shopping_project.onlineshopping.entity.CartItemVO;
import java.util.List;

public interface CartService {
    void add(CartAddDTO dto, Long userId);
    List<CartItemVO> list(Long userId);
    void update(CartUpdateDTO dto, Long userId);
    void delete(Long productId, Long userId);
    String checkout(Long userId, String emailToNotify, String addressJson);
}