package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
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
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<Setmeal> getByCategoryId(long categoryId) {
        return setmealMapper.getByCategoryId(categoryId);
    }

    @Override
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        // 创建套餐实体
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 新增套餐
        setmealMapper.insert(setmeal);
        // 创建套餐菜品关系实体
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            // 给每个套餐关系添加目标套餐Id
            setmealDishes.forEach(setmealDish -> {setmealDish.setSetmealId(setmeal.getId());});
            // 新增套餐菜品关系
            setmealDishMapper.insert(setmealDishes);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.queryPage(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteSetmeal(List<Long> ids) {
        // 判断是否处于停售状态
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        // 删除套餐关系
        setmealDishMapper.delete(ids);
        // 删除套餐
        setmealMapper.delete(ids);
    }

    @Override
    @Transactional
    public void stopOrOpenSetmeal(Integer status, long id) {
        // 若启用套餐，需判断套餐下菜品是否启用
        if (status == StatusConstant.ENABLE) {
            // 判断套装中的菜品是否停用，菜品停用套餐不准启用
            List<SetmealDish> setmealDishs = setmealDishMapper.getBySetmealId(id);
            for (SetmealDish setmealDish : setmealDishs) {
                if (dishMapper.geyById(setmealDish.getDishId()).getStatus() == StatusConstant.DISABLE) {
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        // 更新套餐状态
        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setmealMapper.update(setmeal);
    }

    @Override
    @Transactional
    public SetmealVO getByIdWithSetmealDish(Long id) {
        // 获取套餐信息
        Setmeal setmeal = setmealMapper.getById(id);
        // 获取套餐菜品关系信息
        List<SetmealDish> setmealDishs = setmealDishMapper.getBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        // 构造套餐VO实体
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishs);
        return setmealVO;
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        // 获取套餐ID
        List<Long> ids = new ArrayList<>();
        ids.add(setmealDTO.getId());
        // 删除目标套餐的套餐菜品关系
        setmealDishMapper.delete(ids);
        // 插入新的套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            // 套餐菜品关系实体插入目标套餐Id
            setmealDishes.forEach(setmealDish -> {setmealDish.setSetmealId(setmealDTO.getId());});
            setmealDishMapper.insert(setmealDTO.getSetmealDishes());
        }
        // 更新套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
    }
}
