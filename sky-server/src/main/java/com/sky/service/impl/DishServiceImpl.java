package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    SetmealMapper setmealMapper;

    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    private CategoryMapper categoryMapper;

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
        if (dishFlavorList != null && !dishFlavorList.isEmpty()) {
            // 将菜品ID插入每一个口味中
            dishFlavorList.forEach(dishFlavor -> {dishFlavor.setDishId(dishId);});
            dishFlavorMapper.insert(dishFlavorList);
        }
    }

    @Override
    public PageResult dishPageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> dishVOS = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(dishVOS.getTotal(), dishVOS.getResult());
    }

    @Override
    @Transactional
    public void deleteDish(List<Long> ids) {
        // 查询批量删除Ids中是否有起售的
        ids.forEach(id -> {
            Dish dish = dishMapper.geyById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });
        // 查询批量删除Ids中是否被套餐关联
        long count = setmealMapper.getByIds(ids);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 批量删除口味
        dishFlavorMapper.deleteByIds(ids);
        // 批量删除菜品
        dishMapper.deleteByIds(ids);
    }

    @Override
    @Transactional
    public DishVO getByIdWithFlavor(Long id) {
        // 查询菜品信息
        Dish dish = dishMapper.geyById(id);
        // 根据分类Id查询分类信息
        String categoryName = categoryMapper.getById(dish.getCategoryId()).getName();
        // 根据菜品Id查询菜品对应的口味
        List<DishFlavor> dishFlavorList = dishFlavorMapper.getByDishId(id);
        // 创建菜品VO对象
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavorList);
        dishVO.setCategoryName(categoryName);
        return dishVO;
    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Long dishId = dishDTO.getId();
        // 更新口味表
        // 先删除原来的数据
        List<Long> ids = new ArrayList<>();
        ids.add(dishId);
        dishFlavorMapper.deleteByIds(ids);

        // 再向口味表中插入数据
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if (dishFlavorList != null && !dishFlavorList.isEmpty()) {
            // 将菜品ID插入每一个口味中
            dishFlavorList.forEach(dishFlavor -> {dishFlavor.setDishId(dishId);});
            dishFlavorMapper.insert(dishFlavorList);
        }

        // 更新菜品表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
    }

    @Override
    public void updateDishStatus(Integer status, long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.update(dish);
    }

    @Override
    public List<Dish> listByCategoryId(long categoryId) {
        return dishMapper.listByCategoryId(categoryId);
    }

}
