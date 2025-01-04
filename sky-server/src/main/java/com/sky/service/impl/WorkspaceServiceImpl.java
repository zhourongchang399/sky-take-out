package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/4 16:59
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    UserMapper userMapper;


    @Override
    public BusinessDataVO businessData(LocalDate now) {

        // 时间
        LocalDateTime begin = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(now, LocalTime.MAX);
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);

        // 新增用户数
        Integer userCount = userMapper.userStatistics(map);
        userCount = userCount == null ? 0 : userCount;

        // 总订单数
        Integer totalCount = orderMapper.ordersStatistics(map);
        totalCount = totalCount == null ? 0 : totalCount;

        // 营业额
        map.put("status", Orders.COMPLETED);
        Double turnover = orderMapper.turnoverStatistics(map);
        turnover = turnover == null ? 0 : turnover;

        // 有效订单数
        Integer vaildCount = orderMapper.ordersStatistics(map);
        vaildCount = vaildCount == null ? 0 : vaildCount;

        // 订单完成率
        Double orderCompletionRate = 0.0;
        if (totalCount != 0) {
            orderCompletionRate = (double) vaildCount / (double) totalCount;
        }

        // 平均客单价
        Double unitPrice = 0.0;
        if (vaildCount != 0) {
            unitPrice = turnover / (double) vaildCount;
        }

        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .newUsers(userCount)
                .orderCompletionRate(orderCompletionRate)
                .turnover(turnover)
                .validOrderCount(vaildCount)
                .unitPrice(unitPrice).build();

        return businessDataVO;
    }

    @Override
    public SetmealOverViewVO overviewSetmeals() {
        Integer discontinuedCount = setmealMapper.setmealStatistics(StatusConstant.DISABLE);
        Integer soldCount = setmealMapper.setmealStatistics(StatusConstant.ENABLE);
        SetmealOverViewVO setmealOverViewVO = SetmealOverViewVO.builder()
                .discontinued(discontinuedCount)
                .sold(soldCount)
                .build();
        return setmealOverViewVO;
    }

    @Override
    public DishOverViewVO overviewDishes() {
        Integer discontinuedCount = dishMapper.dishStatistics(StatusConstant.DISABLE);
        Integer soldCount = dishMapper.dishStatistics(StatusConstant.ENABLE);
        DishOverViewVO dishOverViewVO = DishOverViewVO.builder()
                .discontinued(discontinuedCount)
                .sold(soldCount)
                .build();
        return dishOverViewVO;
    }

    @Override
    public OrderOverViewVO overviewOrders() {
        Map<String, Object> map = new HashMap<>();
        Integer allOrders = orderMapper.ordersStatistics(map);
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.ordersStatistics(map);
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.ordersStatistics(map);
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.ordersStatistics(map);
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.ordersStatistics(map);
        OrderOverViewVO orderOverViewVO = OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
        return orderOverViewVO;
    }
}
