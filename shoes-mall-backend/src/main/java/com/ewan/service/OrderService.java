package com.ewan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.exception.BusinessException;
import com.ewan.model.entity.Order;
import com.ewan.model.enums.OrderStatusEnum;
import org.springframework.stereotype.Service;
import com.ewan.mapper.OrderMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    public BaseResponse<Boolean> updateStatus(String ids, OrderStatusEnum nowStatusEnum) {
        String[] arrays = ids.split(",");
        if (arrays.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ids 非法");
        }
        Set<Long> idList = Arrays.stream(arrays).map(Long::parseLong).collect(Collectors.toSet());
        List<Order> orderList = this.list(new QueryWrapper<Order>().in(Order.COL_ID, idList));
        orderList.forEach(r -> {
            r.setOrderStatus(nowStatusEnum.getValue());
        });
        boolean res = this.updateBatchById(orderList);
        return ResultUtils.success(res);
    }
}
