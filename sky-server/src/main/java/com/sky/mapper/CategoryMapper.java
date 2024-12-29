package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2024/12/29 20:35
 */
@Mapper
public interface CategoryMapper {

    public Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
