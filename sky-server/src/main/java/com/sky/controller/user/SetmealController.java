package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/1 23:57
 */
@Slf4j
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @GetMapping("/{id}")
    @ApiOperation("根据套餐id查询包含的菜品")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据套餐id查询包含的菜品:{}", id);
        SetmealVO setmealVO = setmealService.getByIdWithSetmealDish(id);
        return Result.success(setmealVO);
    }

    @GetMapping("/list")
    @ApiOperation("根据分类Id查询套餐")
    public Result<List<Setmeal>> getByCategoryId(Long categoryId) {
        log.info("根据分类Id查询套餐:{}", categoryId);
        List<Setmeal> setmeals = setmealService.getByCategoryId(categoryId);
        return Result.success(setmeals);
    }

}
