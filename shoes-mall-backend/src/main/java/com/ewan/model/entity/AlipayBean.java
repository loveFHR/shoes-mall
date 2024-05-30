package com.ewan.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 支付宝参数
 *
 * @author Ewan
 */
@Data
public class AlipayBean {

    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号，必填
     */
    @NotNull(message = "订单号不能为空")
    private String out_trade_no;

    /**
     * 订单名称，必填
     */
    private String subject = "澎湃鞋城商品购买";

    /**
     * 付款金额，必填
     * 根据支付宝接口协议，必须使用下划线
     */
    private String total_amount;

    /**
     * 超时时间参数
     */
    private String timeout_express = "10m";

    /**
     * 产品编号
     */
    private String product_code = "QUICK_WAP_WAY";
}

