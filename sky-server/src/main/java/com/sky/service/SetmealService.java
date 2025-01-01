package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    List<Setmeal> getByCategoryId(long categoryId);

    void addSetmeal(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteSetmeal(List<Long> ids);

    void stopOrOpenSetmeal(Integer status, long id);

    SetmealVO getByIdWithSetmealDish(Long id);

    void updateSetmeal(SetmealDTO setmealDTO);
}
