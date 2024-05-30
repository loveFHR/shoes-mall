package com.ewan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "product")
public class Product implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为null")
    private Long id;


    /**
     * 系统表品牌id
     */
    @TableField(value = "system_base_setting_brand_id")
    private Long systemBaseSettingBrandId;

    /**
     * 版型
     */
    @TableField(value = "system_base_setting_type_id")
    @NotNull(message = "版型不能为null")
    private Long systemBaseSettingTypeId;

    /**
     * 上下架 0-下架 1-上架 默认0
     */
    @TableField(value = "is_on")
    @NotNull(message = "上下架 0-下架 1-上架 默认0不能为null")
    private Integer isOn;

    /**
     * 用户id
     */
    @TableField(value = "create_by")
    @NotNull(message = "用户id不能为null")
    private Long createBy;

    /**
     * 原价价格扩大100
     */
    @TableField(value = "used_price")
    @NotNull(message = "原价价格扩大100不能为null")
    private Integer usedPrice;

    /**
     * 现价扩大100
     */
    @TableField(value = "now_price")
    @NotNull(message = "现价扩大100不能为null")
    private Integer nowPrice;

    /**
     * 标题
     */
    @TableField(value = "title")
    @Size(max = 32, message = "标题最大长度要小于 32")
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 描述
     */
    @TableField(value = "`desc`")
    @NotBlank(message = "描述不能为空")
    private String desc;

    /**
     * 总货存量
     */
    @TableField(value = "total_num")
    @NotNull(message = "总货存量不能为null")
    private Integer totalNum;

    /**
     * 剩余货量
     */
    @TableField(value = "rest_num")
    @NotNull(message = "剩余货量不能为null")
    private Integer restNum;

    /**
     * 封面
     */
    @TableField(value = "cover")
    @Size(max = 32, message = "封面最大长度要小于 32")
    @NotBlank(message = "封面不能为空")
    private String cover;

    /**
     * 详情主图片 ,分割
     */
    @TableField(value = "main_picture")
    @Size(max = 1024, message = "详情主图片 ,分割最大长度要小于 1024")
    @NotBlank(message = "详情主图片 ,分割不能为空")
    private String mainPicture;


    /**
     * 删除标识字段(0-未删除 1-已删除)
     */
    @TableField(value = "is_deleted")
    @NotNull(message = "删除标识字段(0-未删除 1-已删除)不能为null")
    @TableLogic(value = "0", delval = "1")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";


    public static final String COL_SYSTEM_BASE_SETTING_BRAND_ID = "system_base_setting_brand_id";

    public static final String COL_SYSTEM_BASE_SETTING_TYPE_ID = "system_base_setting_type_id";

    public static final String COL_IS_ON = "is_on";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_USED_PRICE = "used_price";

    public static final String COL_NOW_PRICE = "now_price";

    public static final String COL_TITLE = "title";

    public static final String COL_DESC = "desc";

    public static final String COL_TOTAL_NUM = "total_num";

    public static final String COL_REST_NUM = "rest_num";

    public static final String COL_COVER = "cover";

    public static final String COL_MAIN_PICTURE = "main_picture";


    public static final String COL_IS_DELETED = "is_deleted";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}