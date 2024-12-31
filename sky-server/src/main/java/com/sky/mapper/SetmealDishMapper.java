package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    void insert(@Param(value = "setmealDishes") List<SetmealDish> setmealDishes);

    void delete(List<Long> ids);

    @Select("select * from setmeal_dish where dish_id = #{dishId}")
    List<SetmealDish> getByDishId(long dishId);

    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(long setmealId);
}
