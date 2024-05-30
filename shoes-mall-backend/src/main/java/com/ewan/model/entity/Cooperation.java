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
 * 合作厂家
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "cooperation")
public class Cooperation implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为null")
    private Long id;

    /**
     * 公司名
     */
    @TableField(value = "company_name")
    @Size(max = 16,message = "公司名最大长度要小于 16")
    @NotBlank(message = "公司名不能为空")
    private String companyName;

    /**
     * url
     */
    @TableField(value = "url")
    @Size(max = 32,message = "url最大长度要小于 32")
    @NotBlank(message = "url不能为空")
    private String url;

    /**
     * 图片
     */
    @TableField(value = "cover")
    @Size(max = 32,message = "图片最大长度要小于 32")
    @NotBlank(message = "图片不能为空")
    private String cover;

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

    public static final String COL_COMPANY_NAME = "company_name";

    public static final String COL_URL = "url";

    public static final String COL_COVER = "cover";

    public static final String COL_IS_DELETED = "is_deleted";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}