package com.ewan.constant;

/**
 * 用户常量
 * @author jiangjinagyujia
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    /**
     * 用户注册验证码键
     */
    String VERIFY_CODE = "verify_code";


    /**
     * 验证码内容
     */
    String VERIFY_CODE_EMAIL_CONTENT = "您好，验证码仅5分钟内有效，验证码：";


    /**
     * 验证码标题
     */
    String VERIFY_CODE_EMAIL_TITLE = "邮箱验证码";

    /**
     * 盐
     */
    String SALT = "EWAN";

    String VIP_STR = "20210613";
}
