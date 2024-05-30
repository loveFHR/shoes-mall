package com.ewan.utils;

import java.security.SecureRandom;

/**
 * 要求：密码需要有大写、小写、特殊字符、长度在 8-20 位
 *
 * @author jiangjinagyujia
 */

public class RandomCharUtil {
    private static final String LOW_STR = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_STR = "~!@#$%^&*()_+/-=[]{};:'<>?.";
    private static final String NUM_STR = "0123456789";

    // 随机获取字符串字符
    public static char getRandomChar(String str) {
        SecureRandom random = new SecureRandom();
        return str.charAt(random.nextInt(str.length()));
    }

    // 随机获取小写字符
    public static char getLowChar() {
        return getRandomChar(LOW_STR);
    }

    // 随机获取大写字符
    public static char getUpperChar() {
        return Character.toUpperCase(getLowChar());
    }

    // 随机获取数字字符
    public static char getNumChar() {
        return getRandomChar(NUM_STR);
    }

    // 随机获取特殊字符
    public static char getSpecialChar() {
        return getRandomChar(SPECIAL_STR);
    }

    public static String randomPwd8() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            sb.append(getLowChar());
            sb.append(getUpperChar());
            sb.append(getNumChar());
            sb.append(getSpecialChar());
        }
        return sb.toString();
    }
}

