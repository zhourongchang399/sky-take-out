package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insert(Orders order);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    List<Orders> list(Orders orders);

    void update(Orders orders);

    Orders getById(long id);

    Integer count(Integer status);
}
