package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {

    public long getByCategoryId(long categoryId);

    void addDish(DishDTO dishDTO);
}
