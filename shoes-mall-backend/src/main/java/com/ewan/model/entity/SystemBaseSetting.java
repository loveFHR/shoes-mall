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
 * 系统设置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "system_base_setting")
public class SystemBaseSetting implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "主键不能为null")
    private Long id;

    /**
     * 类型 1鞋码 2颜色 3品牌 4鞋类型
     */
    @TableField(value = "`type`")
    @NotNull(message = "类型 1鞋码 2颜色 3品牌 4鞋类型不能为null")
    private Integer type;

    /**
     * 标签名/类型名
     */
    @TableField(value = "`label`")
    @Size(max = 8, message = "标签名/类型名最大长度要小于 8")
    @NotBlank(message = "标签名/类型名不能为空")
    private String label;

    /**
     * 0-正常 1-禁
     */
    @TableField(value = "`ban`")
    private Integer ban;

    /**
     * 已删除(0否 1是)
     */
    @TableField(value = "deleted")
    @TableLogic(value = "0", delval = "1")
    private Boolean deleted;

    /**
     * 操作人
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_TYPE = "type";

    public static final String COL_LABEL = "label";

    public static final String COL_DELETED = "deleted";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}