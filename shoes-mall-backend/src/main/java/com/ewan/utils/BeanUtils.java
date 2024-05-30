package com.ewan.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Bean 工具类
 */
@Slf4j
public class BeanUtils extends org.springframework.beans.BeanUtils {
    /**
     * Bean方法名中属性名开始的下标
     */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /**
     * 匹配getter方法的正则表达式
     */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /**
     * 匹配setter方法的正则表达式
     */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean属性复制工具方法。
     *
     * @param dest 目标对象
     * @param src  源对象
     */
    public static void copyBeanProp(Object src, Object dest) {
        try {
            copyProperties(src, dest);
        } catch (Exception e) {
            log.error("拷贝属性失败", e);
        }
    }

    /**
     * ignore
     */
    public static <T> T copyProperties(Object source, Class<T> tClass) {
        T target = BeanUtils.instantiateClass(tClass);
        copyProperties(source, target);
        return target;
    }

    /**
     * ignore
     */
    public static <T> T copyProperties(Object source, Class<T> tClass, Consumer<T> targetPeekConsumer) {
        T target = BeanUtils.instantiateClass(tClass);
        copyProperties(source, target);
        targetPeekConsumer.accept(target);
        return target;
    }

    /**
     * 复制集合中的Bean属性<br>
     * 此方法遍历集合中每个Bean，复制其属性后加入一个新的{@link List}中。
     *
     * @param collection 原Bean集合
     * @param targetType 目标Bean类型
     * @param <T>        Bean类型
     * @return 复制后的List
     * @since 5.6.4
     */
    public static <S, T> List<T> copyToList(Collection<S> collection, Class<T> targetType) {
        if (null == collection) {
            return null;
        }
        if (collection.isEmpty()) {
            return new ArrayList<>(0);
        }
        return collection.stream().map((source) -> {
            final T target = BeanUtils.instantiateClass(targetType);
            BeanUtils.copyBeanProp(source, target);
            return target;
        }).collect(Collectors.toList());
    }

    /**
     * 先过滤再复制集合中的Bean属性<br>
     * 此方法遍历集合中每个Bean，复制其属性后加入一个新的{@link List}中。
     *
     * @param collection 原Bean集合
     * @param targetType 目标Bean类型
     * @param <T>        Bean类型
     * @return 复制后的List
     * @since 5.6.4
     */
    public static <S, T> List<T> copyToList(Collection<S> collection, Predicate<S> predicate, Class<T> targetType) {
        if (null == collection) {
            return null;
        }
        if (collection.isEmpty()) {
            return new ArrayList<>(0);
        }
        return collection.stream().filter(predicate).map((source) -> {
            final T target = BeanUtils.instantiateClass(targetType);
            BeanUtils.copyBeanProp(source, target);
            return target;
        }).collect(Collectors.toList());
    }

    /**
     * 拷贝集合
     *
     * @param collection         原始集合
     * @param targetType         目标类型
     * @param targetPeekConsumer 对目标集合内每个元素执行的函数
     * @param <T>                返回的目标类型
     */
    public static <T> List<T> copyToList(Collection<?> collection, Class<T> targetType, Consumer<T> targetPeekConsumer) {
        if (null == collection) {
            return null;
        }
        if (collection.isEmpty()) {
            return new ArrayList<>(0);
        }
        return collection.stream().map(source -> {
            final T target = BeanUtils.instantiateClass(targetType);
            BeanUtils.copyBeanProp(source, target);
            targetPeekConsumer.accept(target);
            return target;
        }).collect(Collectors.toList());
    }

    /**
     * 拷贝集合
     *
     * @param collection         原始集合
     * @param targetType         目标类型
     * @param targetPeekConsumer 对原始集合与目标集合的对应元素进行操作的函数，适用于个别属性需要特殊处理的场景
     * @param <T>                返回的目标类型
     */
    public static <S extends Object, T> List<T> copyToList(Collection<S> collection, Class<T> targetType, BiConsumer<S, T> targetPeekConsumer) {
        if (null == collection) {
            return null;
        }
        if (collection.isEmpty()) {
            return new ArrayList<>(0);
        }
        return collection.stream().map(source -> {
            final T target = BeanUtils.instantiateClass(targetType);
            BeanUtils.copyBeanProp(source, target);
            targetPeekConsumer.accept(source, target);
            return target;
        }).collect(Collectors.toList());
    }

    /**
     * 获取对象的setter方法。
     *
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj) {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods) {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1)) {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     *
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj) {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods) {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0)) {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     *
     * @param m1 方法名1
     * @param m2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(String m1, String m2) {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }
}
