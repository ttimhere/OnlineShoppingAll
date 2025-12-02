package com.online_shopping_project.onlineshopping.entity.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartUpdateDTO {
    @NotNull
    private Long productId; // 新的商品数量
    @Min(0)
    private Integer quantity; // 要修改的商品ID
    private Boolean selected; // 可单独切换选中状态
}