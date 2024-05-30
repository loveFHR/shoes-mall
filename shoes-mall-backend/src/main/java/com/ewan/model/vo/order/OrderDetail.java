package com.ewan.model.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Ewan
 * @date 2024/5/26
 */
@Data
public class OrderDetail {

    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;


    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收件地址
     */
    private String address;


    /**
     * 支付状态:0.未支付,1.支付成功,-1:支付失败
     */
    private Integer payStatus;

    /**
     * 订单状态:0.待支付  2.待发货 3.待收货 4.待评价 5.交易成功 -1.手动关闭 -2.申请退款 -3.退货订单
     */
    private Integer orderStatus;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 数量
     */
    private Integer num;


    /**
     * 标题
     */
    private String title;

    /**
     * 单价
     */
    private Integer nowPrice;


    /**
     * 总价
     */
    private Integer totalPrice;


    /**
     * 封面
     */
    private String cover;


    /**
     * 颜色
     */
    private String colorLabel;

    /**
     * 尺码
     */
    private String sizeLabel;

}

    
    