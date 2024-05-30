package com.ewan.controller;

import com.ewan.common.BaseResponse;
import com.ewan.common.ResultUtils;
import com.ewan.mapper.OrderMapper;
import com.ewan.model.vo.statistic.BaseVo;
import com.ewan.service.SystemBaseSettingService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 报表统计
 *
 * @author Ewan
 * @date 2024/5/26
 */

@Slf4j
@Validated
@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SystemBaseSettingService systemBaseSettingService;

    /**
     * 近七日订单量统计
     *
     * @return vo
     */
    @GetMapping("/order")
    public BaseResponse<List<BaseVo>> order() {
        LocalDateTime endDate = LocalDateTime.now();
        // 计算开始日期（100天前）
        LocalDateTime startDate = endDate.minusDays(99);
        List<BaseVo> sqlVo = orderMapper.countOrdersByDateRange(startDate, endDate);
        return ResultUtils.success(sqlVo);
    }


    /**
     * 鞋型销量痛就
     *
     * @return vo
     */
    @GetMapping("/type")
    public BaseResponse<List<BaseVo>> type() {
        List<BaseVo> res = orderMapper.countOrdersByTypeRange();
        return ResultUtils.success(res);
    }


    /**
     * 商品销量统计
     *
     * @return vo
     */
    @GetMapping("/product")
    public BaseResponse<List<BaseVo>> product() {
        List<BaseVo> res = orderMapper.countOrdersByProductRange();
        return ResultUtils.success(res);
    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }
}

    
    