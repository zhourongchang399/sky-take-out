package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    @Select("select count(0) from setmeal where category_id = #{categoryId} ")
    Integer getByCategoryId(long categoryId);

    long getByIds(List<Long> ids);
}
