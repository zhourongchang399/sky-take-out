package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/2 18:31
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    SetmealMapper setmealMapper;

    @Override
    @Transactional
    public void add(ShoppingCartDTO shoppingCartDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();

        // 查询购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        // 判断是否新购物信息已经存在于购物车内
        if (shoppingCartList.size() > 0) {

            // 存在，则执行更新操作
            ShoppingCart shoppingCartItem = shoppingCartList.get(0);

            // 数量加一
            shoppingCartItem.setNumber(shoppingCartItem.getNumber() + 1);

            // 更新购物车信息
            shoppingCartMapper.update(shoppingCartItem);
        } else {

            // 判断新数据是菜品还是套餐
            if (shoppingCartDTO.getDishId() != null) {
                // 查询菜品信息
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 查询套餐信息
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
            }

            // 不存在，则执行插入操作
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> listById(Long userId) {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(ShoppingCart.builder().userId(userId).build());
        return shoppingCartList;
    }

    @Override
    public void clean(long userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.delete(shoppingCart);
    }

    @Override
    @Transactional
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        // 查询购物车信息
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);

        // 判断是否存在于购物车且数量为1
        if (shoppingCarts.size() > 0) {
            ShoppingCart shoppingCartItem = shoppingCarts.get(0);
            // 数量为1
            // 删除该信息从购物车
            if (shoppingCartItem.getNumber() == 1) {
                shoppingCartMapper.delete(shoppingCartItem);
            } else if (shoppingCartItem.getNumber() > 1){
                // 数量不为1
                // 更新该信息从购物车
                shoppingCartItem.setNumber(shoppingCartItem.getNumber() - 1);
                shoppingCartMapper.update(shoppingCartItem);
            }
        }

    }

}
