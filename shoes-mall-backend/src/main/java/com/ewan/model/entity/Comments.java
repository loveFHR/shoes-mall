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
 * 帖子评论
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "comments")
public class Comments implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "主键不能为null")
    private Long id;

    /**
     * 订单号
     */
    @TableField(value = "order_id")
    @NotNull(message = "订单号不能为空")
    private Long orderId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @NotNull(message = "用户id不能为null")
    private Long userId;

    /**
     * 顶级父评论的id
     */
    @TableField(value = "pid")
    private Long pid;

    /**
     * 商品id
     */
    @TableField(value = "product_id")
    @NotNull(message = "商品id不能为null")
    private Long productId;

    /**
     * 回复的评论id
     */
    @TableField(value = "answer_id")
    private Long answerId;

    /**
     * 回复的内容
     */
    @TableField(value = "content")
    @Size(max = 255, message = "回复的内容最大长度要小于 255")
    @NotBlank(message = "回复的内容不能为空")
    private String content;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_delete")
    @TableLogic(value = "0", delval = "1")
    private Byte isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @NotNull(message = "创建时间不能为null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @NotNull(message = "更新时间不能为null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_PID = "pid";

    public static final String COL_PRODUCT_ID = "product_id";

    public static final String COL_ANSWER_ID = "answer_id";

    public static final String COL_CONTENT = "content";

    public static final String COL_IS_DELETE = "is_delete";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
    public static final String COL_ORDER_ID = "order_id";
}