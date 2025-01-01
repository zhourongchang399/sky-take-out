package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/1 23:36
 */
@Slf4j
@Api(tags = "分类相关接口")
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @ApiOperation("根据类型返回分类列表")
    @GetMapping("/list")
    public Result<List<Category>> listByType(Integer type) {
        log.info("根据类型返回分类列表:{}",type);
        List<Category> categoryList = categoryService.listByType(type);
        return Result.success(categoryList);
    }

}
