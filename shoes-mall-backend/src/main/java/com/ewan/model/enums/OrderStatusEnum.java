package com.ewan.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ewan
 * @date 2024/5/26
 */
public enum OrderStatusEnum {
    // 订单状态:0.待支付  2.待发货 3.待收货 4.待评价 5.交易成功 -1.手动关闭 -2.申请退款 -3.退货订单 13 全部

    WAIT_PAY("待支付", 0),

    WAIT_SEND("待发货", 2),

    WAIT_RECEIVE("待收货", 3),

    WAIT_COMMENT("待评价", 4),

    SUCCESS("交易成功", 5),

    CANCEL("手动关闭", -1),

    APPLY_REFUND("申请退款", -2),

    REFUND_ORDER("退货订单", -3),

    ALL("全部", 13);


    private final String text;

    private final Integer value;

    OrderStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static OrderStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (OrderStatusEnum anEnum : OrderStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

}

    
    