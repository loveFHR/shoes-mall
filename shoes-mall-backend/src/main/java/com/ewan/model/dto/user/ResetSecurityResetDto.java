package com.ewan.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Ewan
 * @date 2024/5/13
 */
@Data
public class ResetSecurityResetDto {
    /**
     * 邮箱验证返回的验证码token
     */
    @NotEmpty(message = "邮箱验证返回的验证码token不能为空")
    private String token;

    /**
     * 密保答案
     */
    @Size(max = 128,message = "密保答案最大长度要小于 128")
    @NotBlank(message = "密保答案不能为空")
    private String securityAnswer;

    /**
     * 密保问题
     */
    @Size(max = 128,message = "密保问题最大长度要小于 128")
    @NotBlank(message = "密保问题不能为空")
    private String securityQuestion;

    /**
     * 邮箱验证码
     */
    @NotEmpty(message = "邮箱验证码不能为空")
    private String code;
}

    
    