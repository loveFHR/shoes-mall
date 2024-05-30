package com.ewan.model.vo.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ewan.model.vo.address.UserAddressVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * @author Ewan
 * @date 2024/5/10
 */

@Data
public class OrderPageVo {
    /**
     * 订单表主键id
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户主键id
     */
    private Long userId;


    /**
     * 支付状态:0.未支付,1.支付成功,-1:支付失败
     */
    private Integer payStatus;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 商品id
     */
    private Long productId;


    /**
     * 订单状态:0.待支付 1.已支付  2.交易成功 -1.手动关闭 -2.超时关闭
     */
    private Integer orderStatus;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //商品信息
    /**
     * 商品加价格
     */
    private Integer price;

    /**
     * 标题
     */
    private String productName;

    /**
     * 封面
     */
    private String cover;

    /**
     * 收货地址
     */
    private UserAddressVo addressVo;


    /**
     * 鞋码id
     */
    private Long systemBaseSettingSizeId;


    /**
     * 鞋码label
     */
    private String systemBaseSettingSizeLabel;


    /**
     * 颜色
     */
    private Long systemBaseSettingColorId;
    /**
     * 颜色label
     */
    private String systemBaseSettingColorLabel;

    /**
     * 备注
     */
    private String remark;


    /**
     * 数量
     */
    private Integer num;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date commentTime;

    /**
     * 是否评价 0-否 1-是
     */
    private Integer isComment ;
}

    
    