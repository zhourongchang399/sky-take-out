package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void add(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> listById(Long userId);

    void clean(long userId);

    void sub(ShoppingCartDTO shoppingCartDTO);
}
