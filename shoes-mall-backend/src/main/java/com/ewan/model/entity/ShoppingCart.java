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
 * 购物车
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "shopping_cart")
public class ShoppingCart implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为null")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @NotNull(message = "用户id不能为null")
    private Long userId;

    /**
     * 商品id
     */
    @TableField(value = "product_id")
    @NotNull(message = "商品id不能为空")
    private Long productId;

    /**
     * 鞋码
     */
    @TableField(value = "system_base_setting_size")
    @NotNull(message = "鞋码不能为null")
    private Long systemBaseSettingSize;

    /**
     * 颜色
     */
    @TableField(value = "system_base_setting_color")
    @NotNull(message = "颜色不能为null")
    private Long systemBaseSettingColor;

    /**
     * 数量
     */
    @TableField(value = "num")
    @NotNull(message = "数量不能为null")
    private Integer num;

    /**
     * *100单价
     */
    @TableField(value = "price")
    @NotNull(message = "*100单价不能为null")
    private Integer price;

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

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_PRODUCT_ID = "product_id";

    public static final String COL_SYSTEM_BASE_SETTING_SIZE = "system_base_setting_size";

    public static final String COL_SYSTEM_BASE_SETTING_COLOR = "system_base_setting_color";

    public static final String COL_NUM = "num";

    public static final String COL_PRICE = "price";

    public static final String COL_IS_DELETED = "is_deleted";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}