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
 * 收货地址
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_address")
public class UserAddress implements Serializable {
    /**
     * 收货地址id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "收货地址id不能为null")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @NotNull(message = "用户id不能为null")
    private Long userId;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    @Size(max = 16,message = "手机号最大长度要小于 16")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 地址
     */
    @TableField(value = "address")
    @Size(max = 128,message = "地址最大长度要小于 128")
    @NotBlank(message = "地址不能为空")
    private String address;

    /**
     * 姓名
     */
    @TableField(value = "`name`")
    @Size(max = 16,message = "姓名最大长度要小于 16")
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 省
     */
    @TableField(value = "province")
    @Size(max = 16,message = "省最大长度要小于 16")
    @NotBlank(message = "省不能为空")
    private String province;

    /**
     * 市
     */
    @TableField(value = "city")
    @Size(max = 16,message = "市最大长度要小于 16")
    @NotBlank(message = "市不能为空")
    private String city;

    /**
     * 区
     */
    @TableField(value = "district")
    @Size(max = 16,message = "区最大长度要小于 16")
    @NotBlank(message = "区不能为空")
    private String district;

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

    public static final String COL_PHONE = "phone";

    public static final String COL_ADDRESS = "address";

    public static final String COL_NAME = "name";

    public static final String COL_PROVINCE = "province";

    public static final String COL_CITY = "city";

    public static final String COL_DISTRICT = "district";

    public static final String COL_IS_DELETED = "is_deleted";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}