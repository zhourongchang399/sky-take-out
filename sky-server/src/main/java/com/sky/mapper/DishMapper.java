package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    @Select("select count(0) from dish where category_id = #{categoryId}")
    long getByCategoryId(long categoryId);

    @AutoFill
    long insert(Dish dish);
}
