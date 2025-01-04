package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/4 14:34
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        StringJoiner dateJoiner = new StringJoiner(",");
        StringJoiner turnoverJoiner = new StringJoiner(",");
        LocalDate endPlusDay = end.plusDays(1);
        while (!begin.isEqual(endPlusDay)) {
            Map<String, Object> map = new HashMap<>();
            map.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
            map.put("end", LocalDateTime.of(begin, LocalTime.MAX));
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.turnoverStatistics(map);
            dateJoiner.add(begin.toString());
            turnoverJoiner.add(String.valueOf(turnover));
            begin = begin.plusDays(1);
        }
        log.info("{}:{}",dateJoiner.toString(),turnoverJoiner.toString());

        return new TurnoverReportVO(dateJoiner.toString(), turnoverJoiner.toString());
    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        StringJoiner dateJoiner = new StringJoiner(",");
        StringJoiner totalUserJoiner = new StringJoiner(",");
        StringJoiner newUserJoiner = new StringJoiner(",");
        LocalDate endPlusDay = end.plusDays(1);
        while (!begin.isEqual(endPlusDay)) {
            // 统计截至到begin当天最晚时刻的总用户
            Map<String, Object> map = new HashMap<>();
            map.put("end", LocalDateTime.of(begin, LocalTime.MAX));
            Double totalUser = userMapper.userStatistics(map);

            // 统计begin当天新增用户
            map.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
            Double newUser = userMapper.userStatistics(map);

            // 构造字符串
            dateJoiner.add(begin.toString());
            newUserJoiner.add(String.valueOf(newUser));
            totalUserJoiner.add(String.valueOf(totalUser));
            begin = begin.plusDays(1);
        }

        return new UserReportVO(dateJoiner.toString(), totalUserJoiner.toString(), newUserJoiner.toString());
    }

}
