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
 * 用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`user`")
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "用户id不能为null")
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @Size(max = 16,message = "用户名最大长度要小于 16")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    @Size(max = 128,message = "手机号最大长度要小于 128")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @Size(max = 16,message = "邮箱最大长度要小于 16")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    /**
     * 密码md5
     */
    @TableField(value = "`password`")
    @Size(max = 512,message = "密码md5最大长度要小于 512")
    @NotBlank(message = "密码md5不能为空")
    private String password;

    /**
     * 权限 admin user
     */
    @TableField(value = "`role`")
    @Size(max = 16,message = "权限 admin user最大长度要小于 16")
    @NotBlank(message = "权限 admin user不能为空")
    private String role;

    /**
     * 性别 0-女 1-男 2-未知
     */
    @TableField(value = "gender")
    @NotNull(message = "性别 0-男 1-女不能为null")
    private Integer gender;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    @Size(max = 128,message = "头像最大长度要小于 128")
    @NotBlank(message = "头像不能为空")
    private String avatar;

    /**
     * 密保答案
     */
    @TableField(value = "security_answer")
    @Size(max = 128,message = "密保答案最大长度要小于 128")
    @NotBlank(message = "密保答案不能为空")
    private String securityAnswer;

    /**
     * 密保问题
     */
    @TableField(value = "security_question")
    @Size(max = 128,message = "密保问题最大长度要小于 128")
    @NotBlank(message = "密保问题不能为空")
    private String securityQuestion;

    /**
     * 状态 0-正常，1-ban了
     */
    @TableField(value = "ban")
    @NotNull(message = "状态 0-正常，1-ban了不能为null")
    private Integer ban;



    /**
     * 默认收货地址id
     */
    @TableField(value = "default_address_id")
    @NotNull(message = "默认收货地址id不能为null")
    private Long defaultAddressId;

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

    public static final String COL_USERNAME = "username";

    public static final String COL_EMAIL = "email";

    public static final String COL_PASSWORD = "password";

    public static final String COL_ROLE = "role";

    public static final String COL_GENDER = "gender";

    public static final String COL_AVATAR = "avatar";

    public static final String COL_SECURITY_QUESTION = "security_question";

    public static final String COL_SECURITY_ANSWER = "security_answer";

    public static final String COL_DEFAULT_ADDRESS_ID = "default_address_id";

    public static final String COL_IS_DELETED = "is_deleted";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_PHONE = "phone";

    public static final String COL_BAN = "ban";

    public static final String COL_UPDATE_TIME = "update_time";
}