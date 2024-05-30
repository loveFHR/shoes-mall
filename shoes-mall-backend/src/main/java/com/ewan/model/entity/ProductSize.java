package com.ewan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 鞋子尺码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "product_size")
public class ProductSize implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "主键不能为null")
    private Long id;

    /**
     * 系统基础配置id
     */
    @TableField(value = "system_base_setting_id")
    @NotNull(message = "系统基础配置id不能为null")
    private Long systemBaseSettingId;

    /**
     * 鞋子id
     */
    @TableField(value = "product_id")
    @NotNull(message = "鞋子id不能为null")
    private Long productId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @NotNull(message = "创建时间不能为null")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @NotNull(message = "更新时间不能为null")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_SYSTEM_BASE_SETTING_ID = "system_base_setting_id";

    public static final String COL_PRODUCTID = "product_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETE = "is_delete";
}