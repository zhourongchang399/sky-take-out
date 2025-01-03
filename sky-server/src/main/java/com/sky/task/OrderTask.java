package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/3 21:34
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    OrderMapper orderMapper;

    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void processOrderTimeoutTask(){
        // 待付款超过15分钟的订单信息
        List<Orders> ordersList = orderMapper.getOrderByStatusAndTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        log.info("处理超时任务：{}",LocalDateTime.now());
        if (ordersList.size() > 0) {
            for (Orders order : ordersList) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("订单未支付超时，自动取消");
                orderMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void processOrderDeliveryTask(){
        // 仍旧派送中的订单信息
        List<Orders> ordersList = orderMapper.getOrderByStatusAndTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusHours(-1));

        log.info("处理仍旧派送中的订单任务：{}",LocalDateTime.now());
        if (ordersList.size() > 0) {
            for (Orders order : ordersList) {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                order.setCancelReason("订单派送超时，自动完成");
                orderMapper.update(order);
            }
        }
    }


}
