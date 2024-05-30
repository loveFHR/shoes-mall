package com.ewan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ewan.model.entity.Order;
import com.ewan.model.vo.order.WaitReplyProduct;
import com.ewan.model.vo.statistic.BaseVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.elasticsearch.annotations.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    List<BaseVo> countOrdersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<BaseVo> countOrdersByTypeRange();

    List<BaseVo> countOrdersByProductRange();

    List<Long> getWaitReplyOrderIdList();

}