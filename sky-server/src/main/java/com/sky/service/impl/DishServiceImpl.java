package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Target;
import java.util.List;

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

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Override
    public long getByCategoryId(long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        // 向菜品表中插入数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        // 获取当前菜品ID
        long dishId = dish.getId();
        // 向口味表中插入数据
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if (dishFlavorList != null && dishFlavorList.size() > 0) {
            // 将菜品ID插入每一个口味中
            dishFlavorList.forEach(dishFlavor -> {dishFlavor.setDishId(dishId);});
        }
        dishFlavorMapper.insert(dishFlavorList);
    }
}
