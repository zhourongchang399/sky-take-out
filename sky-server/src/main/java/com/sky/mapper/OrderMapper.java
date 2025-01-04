package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders order);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    List<Orders> list(Orders orders);

    void update(Orders orders);

    Orders getById(long id);

    Integer count(Integer status);

    List<Orders> getOrderByStatusAndTimeLT(Integer status, LocalDateTime orderTime);

    Double turnoverStatistics(Map<String, Object> map);
}
