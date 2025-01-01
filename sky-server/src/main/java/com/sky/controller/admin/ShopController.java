package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/1 17:19
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    @Autowired
    RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("更新店铺状态")
    public Result updateStatus(@PathVariable Integer status) {
        log.info("更新店铺状态：{}", status == StatusConstant.ENABLE ? "闭店" : "开店");
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(RedisConstant.STATUS, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus() {
        ValueOperations operations = redisTemplate.opsForValue();
        Integer status = (Integer) operations.get(RedisConstant.STATUS);
        log.info("获取店铺状态：{}", status == StatusConstant.ENABLE ? "闭店" : "开店");
        return Result.success(status);
    }

}
