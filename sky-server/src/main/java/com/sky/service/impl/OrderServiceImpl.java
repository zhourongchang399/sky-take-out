package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailsMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderHistoryVO;
import com.sky.vo.OrderItemVO;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/3 13:30
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 获取当前用户Id
        Long userId = BaseContext.getCurrentId();

        // 获取地址薄信息
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(userId);
        addressBook.setId(ordersSubmitDTO.getAddressBookId());
        List<AddressBook> addressBooks = addressBookMapper.list(addressBook);

        // 获取购物车信息
        ShoppingCart shoppingCart = ShoppingCart.builder()
                                                .userId(userId)
                                                .build();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);

        // 判断地址薄和购物车是否为空
        if (addressBooks.size() == 0) {
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        if (shoppingCarts.size() == 0) {
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 获取当前地址薄信息
        AddressBook userAddressBook = addressBooks.get(0);

        // 构建订单对象
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);

        // 补全订单对象
        order.setUserId(userId);
        order.setOrderTime(LocalDateTime.now());
        order.setPayStatus(Orders.UN_PAID);
        order.setPhone(userAddressBook.getPhone());
        order.setAddress(userAddressBook.getDetail());
        order.setConsignee(userAddressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setStatus(Orders.PENDING_PAYMENT);

        // 插入订单数据，返回自增orderId
        orderMapper.insert(order);

        // 插入订单明细
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetails.add(orderDetail);
        }
        orderDetailsMapper.insert(orderDetails);

        // 清空购物车
        ShoppingCart userShoppingCart = new ShoppingCart();
        userShoppingCart.setUserId(userId);
        shoppingCartMapper.delete(userShoppingCart);

        // 构建订单VO对象
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    @Override
    @Transactional
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 分页查询订单信息
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        List<Orders> OrderList = page.getResult();

        // 构建历史订单实体
        List<OrderHistoryVO> orderHistoryVOList = new ArrayList<>();
        for (Orders orders : OrderList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            List<OrderDetail> orderDetails = orderDetailsMapper.list(orderDetail);
            OrderHistoryVO orderHistoryVO = new OrderHistoryVO();
            BeanUtils.copyProperties(orders, orderHistoryVO);
            orderHistoryVO.setOrderDetailList(orderDetails);
            orderHistoryVOList.add(orderHistoryVO);
        }
        return new PageResult(page.getTotal(), orderHistoryVOList);
    }

    @Override
    public void cancel(long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.CANCELLED);
        orderMapper.update(orders);
    }

    @Override
    @Transactional
    public OrderItemVO orderDetail(long id) {
        // 查询订单信息
        Orders orders = orderMapper.getById(id);
        OrderItemVO orderItemVO = new OrderItemVO();
        BeanUtils.copyProperties(orders, orderItemVO);

        // 查询订单详细信息
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(id);
        List<OrderDetail> orderDetailList = orderDetailsMapper.list(orderDetail);

        orderItemVO.setOrderDetailList(orderDetailList);
        return orderItemVO;
    }
}
