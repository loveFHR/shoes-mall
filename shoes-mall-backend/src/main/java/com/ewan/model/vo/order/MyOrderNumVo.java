package com.ewan.model.vo.order;

import lombok.Data;

/**
 * @author Ewan
 * @date 2024/5/26
 */
@Data
public class MyOrderNumVo {
    /**
     * 待支付
     */
    private Long waitPay;
    /**
     * 待发货
     */
    private Long waitSend;
    /**
     * 待收货
     */
    private Long waitReceive;
    /**
     * 待评价
     */
    private Long waitComment;
}

    
    