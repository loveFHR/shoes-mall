package com.ewan.model.dto.user;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户登录请求
 */
@Data
public class UserLoginDto {


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

    @NotNull
    private Integer admin;
}
