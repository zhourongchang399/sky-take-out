package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
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
    ReportMapper reportMapper;

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
            Double turnover = reportMapper.turnoverStatistics(map);
            begin = begin.plusDays(1);
            dateJoiner.add(begin.toString());
            turnoverJoiner.add(String.valueOf(turnover));
        }
        log.info("{}:{}",dateJoiner.toString(),turnoverJoiner.toString());

        return new TurnoverReportVO(dateJoiner.toString(), turnoverJoiner.toString());
    }

}
