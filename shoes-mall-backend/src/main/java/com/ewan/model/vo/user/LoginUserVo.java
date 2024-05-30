package com.ewan.model.vo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Ewan
 * @date 2024/5/3
 */

@Data
public class LoginUserVo {
    /**
     * id
     */
    private Long id;

    /**
     * 邮箱（用户名 唯一）
     */
    private String email;

    /**
     * 用户昵称
     */
    private String username;

    private String token;


    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别 0-女 1-男 2-保密
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态 0-正常，1-ban了
     */
    private Integer ban;

    /**
     * 用户角色 user-普通用户,admin-管理员
     */
    private String role;


    /**
     * 密保问题
     */
    private String securityQuestion;
}

    
    