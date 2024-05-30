package com.ewan.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.ewan.annotation.AuthCheck;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.config.AlipayConfig;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.order.RefundDto;
import com.ewan.model.entity.AlipayBean;
import com.ewan.model.entity.Order;
import com.ewan.model.enums.OrderStatusEnum;
import com.ewan.service.OrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿里支付
 *
 * @author Ewan
 */
@RestController
@RequestMapping("/pay")
@CrossOrigin
@Slf4j
public class PayController {

    @Resource
    private AlipayConfig alipayConfig;

    @Resource
    private OrderService orderService;


    @Resource
    private AlipayClient alipayClient;


    /**
     * 阿里支付
     *
     * @param alipayBean 参数
     * @return 信息
     */
    @PostMapping("/alipay")
    public BaseResponse<String> alipay(@RequestBody @Validated AlipayBean alipayBean) {
        List<Order> orderList = orderService.list(new QueryWrapper<Order>().eq(Order.COL_ORDER_NO, alipayBean.getOut_trade_no()));
        if (orderList.size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "订单不存在");
        }
        Integer totalPrice = orderList.stream().map(Order::getTotalPrice).reduce(0, Integer::sum);

        alipayBean.setTotal_amount(totalPrice.toString());
        //TODO
        String returnUrl = alipayConfig.getReturnUrl();
        String notifyUrl = alipayConfig.getNotifyUrl();
        System.out.println(alipayBean);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);
        alipayRequest.setBizContent(JSON.toJSONString(alipayBean));
        System.out.println("--------请求参数--------");
        System.out.println(alipayRequest.getBizContent());
        String result;
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "pageExecute失败");
        }
        return ResultUtils.success(result);
    }


    /**
     * 支付宝回调
     *
     * @return 成功
     */
    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) {
        if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");

            List<Order> orderList = orderService.list(new QueryWrapper<Order>().eq(Order.COL_ORDER_NO, tradeNo));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            try {
                // 解析日期时间字符串为Date对象
                date = sdf.parse(gmtPayment);
            } catch (ParseException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "解析支付时间失败");
            }

            List<Order> updateList = new ArrayList<>();
            for (Order order : orderList) {
                // 更新订单未已支付
                order.setPayStatus(1);
                order.setPayTime(date);
                order.setOrderStatus(2);
                updateList.add(order);
            }
            orderService.updateBatchById(updateList);
        }
        return "success";
    }


    /**
     * 退款
     *
     * @return 退款失败的订单号（,） 为空 表示 成功
     */
    @PostMapping("/refund")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> toRefund(@RequestBody @Validated RefundDto dto) {
        if (dto == null || dto.getOrderNoList() == null || dto.getOrderNoList().size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "必填订单号");
        }

        Map<String, Order> orderMap = SimpleQuery.keyMap(new LambdaQueryWrapper<Order>().in(Order::getOrderNo, dto.getOrderNoList()), Order::getOrderNo);
        List<Order> successOrderList = new ArrayList<>();
        List<String> failOrderNoList = new ArrayList<>();
        for (String orderNo : dto.getOrderNoList()) {
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            String refundAmount = String.valueOf(orderMap.get(orderNo).getTotalPrice());
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + orderNo + "\","
                    + "\"refund_amount\":\"" + refundAmount + "\"}");
            String res = null;
            try {
                res = alipayClient.execute(alipayRequest).getBody();
                Order order = orderMap.get(orderNo);
                order.setOrderStatus(OrderStatusEnum.REFUND_ORDER.getValue());
                successOrderList.add(order);
            } catch (AlipayApiException e) {
                log.error("退款失败" + e.getErrMsg());
                failOrderNoList.add(orderNo);
            }
            System.out.println(orderNo + "---------" + res);
        }
        //更新状态
        orderService.updateBatchById(successOrderList);
        return ResultUtils.success(failOrderNoList.size() == 0 ? "" : StringUtils.join(failOrderNoList, ","));
    }
}

