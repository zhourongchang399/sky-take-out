package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.github.pagehelper.Page;

import java.util.List;

@Mapper
public interface DishMapper {

    @Select("select count(0) from dish where category_id = #{categoryId}")
    long getByCategoryId(long categoryId);

    @AutoFill
    long insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish geyById(Long id);

    void deleteByIds(List<Long> ids);

    @AutoFill(operation = OperationType.UPDATE)
    void update(Dish dish);
}
