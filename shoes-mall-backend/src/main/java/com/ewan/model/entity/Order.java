package com.ewan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单
 * @author jiangjinagyujia
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`order`")
public class Order implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为null")
    private Long id;

    /**
     * 商品id
     */
    @TableField(value = "product_id")
    @NotNull(message = "商品id不能为null")
    private Long productId;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    @Size(max = 16, message = "订单号最大长度要小于 16")
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @NotNull(message = "用户id不能为null")
    private Long userId;

    /**
     * 鞋码id
     */
    @TableField(value = "system_base_setting_size_id")
    @NotNull(message = "鞋码id不能为null")
    private Long systemBaseSettingSizeId;

    /**
     * 颜色
     */
    @TableField(value = "system_base_setting_color_id")
    @NotNull(message = "颜色不能为null")
    private Long systemBaseSettingColorId;

    /**
     * 1-微信支付 2-支付宝
     */
    @TableField(value = "pay_type")
    private Integer payType;

    /**
     * 订单总价 *100
     */
    @TableField(value = "total_price")
    @NotNull(message = "订单总价 *100不能为null")
    private Integer totalPrice;

    /**
     * 支付状态:0.未支付,1.支付成功,-1:支付失败
     */
    @TableField(value = "pay_status")
    @NotNull(message = "支付状态:0.未支付,1.支付成功,-1:支付失败不能为null")
    private Integer payStatus;

    /**
     * 订单状态:0.待支付  2.待发货 3.待收货 4.待评价 5.交易成功 -1.手动关闭 -2.申请退款 -3.退货订单
     */
    @TableField(value = "order_status")
    @NotNull(message = "订单状态:0.待支付  2.待发货 3.待收货 4.待评价 5.交易成功 -1.手动关闭 -2.申请退款 -3.退货订单 不能为空")
    private Integer orderStatus;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date payTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @Size(max = 100, message = "备注最大长度要小于 100")
    @NotBlank(message = "备注不能为空")
    private String remark;

    /**
     * 删除标识字段(0-未删除 1-已删除)
     */
    @TableField(value = "is_deleted")
    @NotNull(message = "删除标识字段(0-未删除 1-已删除)不能为null")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @NotNull(message = "创建时间不能为null")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @NotNull(message = "修改时间不能为null")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     * 地址
     */
    @TableField(value = "address_id")
    @NotNull(message = "地址不能为null")
    private Long addressId;

    /**
     * 数量
     */
    @TableField(value = "num")
    @NotNull(message = "数量不能为空")
    private Integer num;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_PRODUCT_ID = "product_id";

    public static final String COL_ORDER_NO = "order_no";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_SYSTEM_BASE_SETTING_SIZE_ID = "system_base_setting_size_id";

    public static final String COL_SYSTEM_BASE_SETTING_COLOR_ID = "system_base_setting_color_id";

    public static final String COL_PAY_TYPE = "pay_type";

    public static final String COL_TOTAL_PRICE = "total_price";

    public static final String COL_PAY_STATUS = "pay_status";

    public static final String COL_ORDER_STATUS = "order_status";

    public static final String COL_PAY_TIME = "pay_time";

    public static final String COL_REMARK = "remark";

    public static final String COL_IS_DELETED = "is_deleted";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_ADDRESS_ID = "address_id";

    public static final String COL_NUM = "num";
}