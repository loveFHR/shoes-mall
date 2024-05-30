package com.ewan.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author Ewan
 * @date 2024/5/3
 */

@Data
public class RegisterDto {
    /**
     * 邮箱（用户名）
     */
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱非法")
    private String email;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度应在6-16位之间")
    private String password;

    /**
     * 确认密码
     */
    @NotEmpty(message = "确认密码不能为空")
    @Length(min = 6, max = 16, message = "确认密码长度应在6-16位之间")
    private String checkPassword;


    /**
     * 用户昵称
     */
    @Size(max = 255, message = "用户昵称最大长度要小于 255")
    @NotBlank(message = "用户昵称不能为空")
    private String username;

    /**
     * 性别 0-女 1-男 2-保密
     */
    @NotNull(message = "性别 0-女 1-男 不能为null")
    private Integer gender;

    /**
     * 手机号
     */
    @Size(max = 128, message = "手机号最大长度要小于 128")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 邮箱验证码
     */
    @NotEmpty(message = "邮箱验证码不能为空")
    private String code;


    /**
     * 验证码token
     */
    @NotEmpty(message = "验证码token不能为空")
    private String token;


    /**
     * 头像
     */
    private String avatar;


    /**
     * 密保问题
     */
    @Size(max = 128, message = "密保问题最大长度要小于 128")
    @NotBlank(message = "密保问题不能为空")
    private String securityQuestion;

    /**
     * 密保答案
     */
    @Size(max = 128, message = "密保答案最大长度要小于 128")
    @NotBlank(message = "密保答案不能为空")
    private String securityAnswer;
}

    
    