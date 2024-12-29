package com.sky.service.impl;

import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2024/12/29 21:37
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public long getByCategoryId(long categoryId) {
        return setmealMapper.getByCategoryId(categoryId);
    }
}
