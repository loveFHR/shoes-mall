package com.ewan.model.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;

/**
 * @author Ewan
 * @date 2024/5/3
 */
@Data
public class UserUpdateDto {

//    /**
//     * 邮箱（用户名）
//     */
//    @NotEmpty(message = "邮箱不能为空")
//    @Email(message = "邮箱非法")
//    private String email;


    /**
     * 用户昵称
     */
    @Size(max = 255, message = "用户昵称最大长度要小于 255")
    @NotBlank(message = "用户昵称不能为空")
    private String username;

    /**
     * 性别 0-女 1-男 2-保密
     */
    @NotNull(message = "性别 0-女 1-男 2-保密不能为null")
    private Integer gender;

    @NotEmpty(message = "头像不能为空")
    private String avatar;

    /**
     * 手机号
     */
    @Size(max = 128, message = "手机号最大长度要小于 128")
    @NotBlank(message = "手机号不能为空")
    private String phone;
}

    
    