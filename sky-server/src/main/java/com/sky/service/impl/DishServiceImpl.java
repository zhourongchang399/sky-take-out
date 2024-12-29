package com.sky.service.impl;

import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2024/12/29 21:37
 */
@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Override
    public long getByCategoryId(long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }
}
