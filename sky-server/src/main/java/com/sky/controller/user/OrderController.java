package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderItemVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/3 13:27
 */
@RequestMapping("/user/order")
@RestController("userOrderController")
@Slf4j
@Api(tags = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("提交订单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("提交订单:{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("历史订单查询:{}",ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable long id) {
        log.info("取消订单:{}",id);
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.CANCELLED);
        orders.setPayStatus(Orders.REFUND);
        orders.setCancelTime(LocalDateTime.now());
        orderService.update(orders);
        return Result.success();
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("订单细节")
    public Result<OrderItemVO> orderDetail(@PathVariable long id) {
        log.info("订单细节:{}",id);
        OrderItemVO orderItemVO = orderService.orderDetail(id);
        return Result.success(orderItemVO);
    }

    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repitition(@PathVariable long id) {
        log.info("再来一单:{}",id);
        orderService.repitition(id);
        return Result.success();
    }

    @PutMapping("/payment")
    @ApiOperation("支付订单")
    public Result<Orders> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        log.info("支付订单:{}",ordersPaymentDTO);
        Orders orders = orderService.payment(ordersPaymentDTO);
        return Result.success(orders);
    }

}
