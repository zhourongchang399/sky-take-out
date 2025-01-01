package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
 * @date ：2025/1/1 23:55
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    @ApiOperation("根据分类Id查询菜品")
    public Result<List<DishVO>> listById(long categoryId) {
        log.info("根据分类Id查询菜品:{}",categoryId);
        List<DishVO> dishList = dishService.listByCategoryIdWithFlavors(categoryId);
        return Result.success(dishList);
    }

}
