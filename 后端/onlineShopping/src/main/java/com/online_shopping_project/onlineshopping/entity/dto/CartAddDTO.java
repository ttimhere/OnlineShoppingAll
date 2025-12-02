package com.online_shopping_project.onlineshopping.entity.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartAddDTO {
    @NotNull
    private Long productId;  // 商品ID
    @NotNull @Min(1)
    private Integer quantity;// 数量
}