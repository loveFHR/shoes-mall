package com.ewan.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Ewan
 * @date 2024/5/12
 */

@Data
public class ResetPwdDto {


    /**
     * 密保答案
     */
    @TableField(value = "security_answer")
    @Size(max = 128,message = "密保答案最大长度要小于 128")
    @NotBlank(message = "密保答案不能为空")
    private String securityAnswer;


//    /**
//     * 原密码
//     */
//    @Length(min = 6, max = 16, message = "密码长度应在6-16位之间")
//    @NotEmpty(message = "原密码不能为空")
//    private String pwd;


    /**
     * 现密码
     */
    @Length(min = 6, max = 16, message = "密码长度应在6-16位之间")
    @NotEmpty(message = "现密码不能为空")
    private String resetPwd;


    /**
     * 现密码 二次输入
     */
    @Length(min = 6, max = 16, message = "密码长度应在6-16位之间")
    @NotEmpty(message = "确认密码不能为空")
    private String checkPwd;
}

    
    