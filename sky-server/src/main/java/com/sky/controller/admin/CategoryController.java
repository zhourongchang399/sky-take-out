package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2024/12/29 20:28
 */
@Slf4j
@Api(tags = "分类相关接口")
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @ApiOperation("分类分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("新增分类")
    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    @ApiOperation("开启或停用分类")
    @PostMapping("/status/{status}")
    public Result updateCategoryStatus(@PathVariable int status,long id) {
        log.info("开启或停用分类：{},{}",status, id);
        categoryService.updateCategoryStatus(status, id);
        return Result.success();
    }

    @ApiOperation("修改分类")
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @ApiOperation("根据ID删除分类")
    @DeleteMapping
    public Result deleteById(long id) {
        log.info("根据ID删除分类:{}",id);
        categoryService.deleteById(id);
        return Result.success();
    }

    @ApiOperation("根据类型返回分类列表")
    @GetMapping("/list")
    public Result<List<Category>> listByType(Integer type) {
        log.info("根据类型返回分类列表:{}",type);
        List<Category> categoryList = categoryService.listByType(type);
        return Result.success(categoryList);
    }

}
