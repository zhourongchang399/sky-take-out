package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderItemVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    void update(Orders orders);

    OrderItemVO orderDetail(long id);

    void repitition(long id);

    Orders payment(OrdersPaymentDTO ordersPaymentDTO);

    OrderOverViewVO statistics();
}
