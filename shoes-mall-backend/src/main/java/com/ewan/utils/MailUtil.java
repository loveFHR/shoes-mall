package com.ewan.utils;

import com.ewan.common.ErrorCode;
import com.ewan.exception.BusinessException;
import com.ewan.model.entity.MailBean;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送邮件
 */
@Component
@RequiredArgsConstructor
public class MailUtil {
    /**
     * 邮件发送者
     */
    @Value("${spring.mail.username}")
    private String MAIL_SENDER;

    /**
     * 注入QQ发送邮件的bean
     */
    private final JavaMailSender javaMailSender;

    public void sendMail(String mail, String content,String title) {
        MailBean mailBean = new MailBean();
        //接收者
        mailBean.setRecipient(mail);
        //标题
        mailBean.setSubject(title);
        //内容主体
        mailBean.setContent(content);
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            //发送者
            mailMessage.setFrom(MAIL_SENDER);
            //接收者
            mailMessage.setTo(mailBean.getRecipient());
            //邮件标题
            mailMessage.setSubject(mailBean.getSubject());
            //邮件内容
            mailMessage.setText(mailBean.getContent());
            //发送邮箱
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "邮件发送失败");
        }
    }


    /**
     * 判断该邮件地址是否合法
     *
     * @param address 邮件地址，可以多个，逗号隔开
     * @return 是否是邮箱地址
     */
    public static boolean isEmailAddress(String address) {
        // 是否合法
        boolean flag = false;
        if (StringUtils.isEmpty(address)) {
            return false;
        }
        try {
            String[] addressArr = address.split(",");
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher;
            for (String str : addressArr) {
                matcher = regex.matcher(str);
                flag = matcher.matches();
                if (!flag) {
                    return false;
                }
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 生成6位数字验证码
     *
     * @return 验证码
     */
    public static String getVerifyCode() {
        return String.valueOf((int) (Math.random() * 900000 + 100000));
    }
}