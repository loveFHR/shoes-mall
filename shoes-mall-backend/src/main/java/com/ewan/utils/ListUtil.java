package com.ewan.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author junLin
 * @version 1.0
 * @since 2023-5-24 14:04
 */
public class ListUtil extends CollectionUtils {

    /**
     * 集合是否不为空，集合不为 null 且size>0 则返回 true否则返回false
     */
    public static boolean nonEmpty(@Nullable Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean nonEmpty(@Nullable Object[] collection) {
        return collection != null && collection.length > 0;
    }

    /**
     * 集合是否不为空，集合不为 null 且size>0 则返回 true否则返回false
     */
    public static boolean nonEmpty(@Nullable Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 如果集合不为空则执行函数
     *
     * @param collection 集合
     * @param consumer   函数
     */
    public static <U extends Collection<T>, T> void nonEmpty(@Nullable U collection, Consumer<U> consumer) {
        if (nonEmpty(collection)) {
            consumer.accept(collection);
        }
    }

    /**
     * 如果Map不为空则执行函数
     *
     * @param map      键值对
     * @param consumer 函数
     */
    public static <K, V> void nonEmpty(@Nullable Map<K, V> map, Consumer<Map<K, V>> consumer) {
        if (Objects.nonNull(map) && !map.isEmpty()) {
            consumer.accept(map);
        }
    }

    /**
     * 过滤集合
     *
     * @param collection 原始集合
     * @param predicates 过滤器
     */
    @SafeVarargs
    public static <T> List<T> filter(@Nullable Collection<T> collection, Predicate<? super T>... predicates) {
        if (nonEmpty(collection)) {
            Stream<T> stream = collection.stream();
            for (Predicate<? super T> predicate : predicates) {
                stream = stream.filter(predicate);
            }
            return stream.collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    /**
     * 安全的循环，即使collection 为null也不会报错
     * <p>
     * 如果集合中有元素为空则跳过
     *
     * @param collection 集合
     * @param consumer   消费函数
     */
    public static <T> void safeEach(@Nullable Collection<T> collection, Consumer<T> consumer) {
        if (nonEmpty(collection)) {
            collection.stream().filter(Objects::nonNull).forEach(consumer);
        }
    }

    /**
     * 安全的循环，即使map 为null也不会报错
     *
     * @param map      集合
     * @param consumer 消费函数
     */
    public static <K, V> void safeEach(@Nullable Map<K, V> map, BiConsumer<K, V> consumer) {
        if (nonEmpty(map)) {
            map.forEach(consumer);
        }
    }

    /**
     * 安全的遍历
     *
     * @param collection 集合
     * @param consumer   对满足条件的元素运用的函数
     * @param predicate  集合中元素需满足的条件
     */
    public static <T> void safeEach(@Nullable Collection<T> collection, Predicate<? super T> predicate, Consumer<T> consumer) {
        if (nonEmpty(collection)) {
            collection.stream().filter(predicate).forEach(consumer);
        }
    }

    /**
     * 如果集合不为空则映射为新的类型
     *
     * @param collection 原始集合
     * @param mapper     映射函数
     * @param <T>        原始泛型
     * @param <U>        新泛型
     * @return 将原始类型按mapper的方式转换为新类型
     */
    public static <T, U> List<U> map(@Nullable Collection<T> collection, Function<T, U> mapper) {
        if (nonEmpty(collection)) {
            return collection.stream().map(mapper).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    /**
     * 如果集合不为空则映射为新的类型
     *
     * @param collection   原始集合
     * @param mapper       映射函数
     * @param downConsumer 下游操作
     * @param <T>          原始泛型
     * @param <U>          新泛型
     * @return 将原始类型按mapper的方式转换为新类型
     */
    public static <T, U> List<U> map(@Nullable Collection<T> collection, Function<T, U> mapper, Consumer<List<U>> downConsumer) {
        if (nonEmpty(collection)) {
            List<U> list = collection.stream().map(mapper).collect(Collectors.toList());
            downConsumer.accept(list);
            return list;
        }
        return new ArrayList<>(0);
    }

    /**
     * 如果集合不为空则过滤后映射为新的类型
     *
     * @param collection 原始集合
     * @param predicate  过滤函数
     * @param mapper     映射函数
     * @param <T>        原始泛型
     * @param <U>        新泛型
     * @return 将原始类型按mapper的方式转换为新类型
     */
    public static <T, U> List<U> filterMap(@Nullable Collection<T> collection, Predicate<T> predicate, Function<T, U> mapper) {
        if (nonEmpty(collection)) {
            return collection.stream().filter(predicate).map(mapper).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    /**
     * 如果集合不为空则过滤后映射为新的类型
     *
     * @param collection 原始集合
     * @param predicate  过滤函数
     * @param mapper     映射函数
     * @param <T>        原始泛型
     * @param <U>        新泛型
     * @return 将原始类型按mapper的方式转换为新类型
     */
    public static <T, U> List<U> filterMap(@Nullable Collection<T> collection, Predicate<T> predicate, Function<T, U> mapper, Consumer<List<U>> downConsumer) {
        if (nonEmpty(collection)) {
            List<U> list = collection.stream().filter(predicate).map(mapper).collect(Collectors.toList());
            downConsumer.accept(list);
            return list;
        }
        return new ArrayList<>(0);
    }


    /**
     * 将集合转换为Map
     *
     * @param collection     集合
     * @param keyFunc        map key 映射器
     * @param <T>            集合的泛型
     * @param <K>            Map的key泛型
     * @return Map
     */
    public static <T, K> Map<K, T> listToMap(@Nullable Collection<T> collection, Function<T, K> keyFunc) {
        if (nonEmpty(collection)) {
            HashMap<K, T> hashMap = new HashMap<>(collection.size(), 1);
            collection.forEach(item -> hashMap.put(keyFunc.apply(item), item));
            return hashMap;
        }
        return new HashMap<>(0, 1);
    }

    /**
     * 将集合转换为Map
     *
     * @param collection     集合
     * @param keyFunc        map key 映射器
     * @param valueFunc      map value 映射器
     * @param <T>            集合的泛型
     * @param <K>            Map的key泛型
     * @param <V>Map的value泛型
     * @return Map
     */
    public static <T, K, V> Map<K, V> listToMap(@Nullable Collection<T> collection, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (nonEmpty(collection)) {
            HashMap<K, V> hashMap = new HashMap<>(collection.size(), 1);
            collection.forEach(item -> hashMap.put(keyFunc.apply(item), valueFunc.apply(item)));
            return hashMap;
        }
        return new HashMap<>(0, 1);
    }

    /**
     * 将集合转换为Map然后应用下游消费者函数并返回这个map
     *
     * @param collection     集合
     * @param keyFunc        map key 映射器
     * @param valueFunc      map value 映射器
     * @param downConsumer   下游消费者
     * @param <T>            集合的泛型
     * @param <K>            Map的key泛型
     * @param <V>Map的value泛型
     * @return Map
     */
    public static <T, K, V> Map<K, V> listToMap(@Nullable Collection<T> collection, Function<T, K> keyFunc, Function<T, V> valueFunc, Consumer<Map<K, V>> downConsumer) {
        if (nonEmpty(collection)) {
            Map<K, V> map = listToMap(collection, keyFunc, valueFunc);
            downConsumer.accept(map);
            return map;
        }
        return new HashMap<>(0, 1);
    }

    /**
     * 将集合先进行上游映射后转换为Map
     *
     * @param collection     集合
     * @param upstreamFunc   上游映射
     * @param keyFunc        map key 映射器
     * @param valueFunc      map value 映射器
     * @param <S>            原始集合的类型
     * @param <T>            上游操作返回的类型
     * @param <K>            Map的key类型
     * @param <V>Map的value类型
     * @return Map
     */
    public static <S, T, K, V> Map<K, V> listToMap(@Nullable Collection<S> collection, Function<Collection<S>, List<T>> upstreamFunc, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (nonEmpty(collection)) {
            return listToMap(upstreamFunc.apply(collection), keyFunc, valueFunc);
        }
        return new HashMap<>(0, 1);
    }

    /**
     * 将集合先进行上游映射后转换为Map然后应用下游消费者函数并返回这个map
     *
     * @param collection     集合
     * @param upstreamFunc   上游映射
     * @param keyFunc        map key 映射器
     * @param valueFunc      map value 映射器
     * @param downConsumer   下游消费者
     * @param <S>            原始集合的类型
     * @param <T>            上游操作返回的类型
     * @param <K>            Map的key类型
     * @param <V>Map的value类型
     * @return Map
     */
    public static <S, T, K, V> Map<K, V> listToMap(@Nullable Collection<S> collection, Function<Collection<S>, List<T>> upstreamFunc, Function<T, K> keyFunc, Function<T, V> valueFunc, Consumer<Map<K, V>> downConsumer) {
        if (nonEmpty(collection)) {
            Map<K, V> map = listToMap(upstreamFunc.apply(collection), keyFunc, valueFunc);
            downConsumer.accept(map);
            return map;
        }
        return new HashMap<>(0, 1);
    }

    /**
     * 将集合分组
     *
     * @param collection 待分组的集合
     * @param keyFunc    map key映射器
     * @param <K>        key类型
     * @param <V>        value类型
     */
    public static <K, V> Map<K, List<V>> listGroup(@Nullable Collection<V> collection, Function<V, K> keyFunc) {
        if (nonEmpty(collection)) {
            return collection.stream().collect(Collectors.groupingBy(keyFunc));
        }
        return new HashMap<>(0, 1);
    }

    /**
     * 将集合分组并映射组内每一个元素
     *
     * @param collection 集合
     * @param keyFunc    map key
     * @param valueFunc  元素映射函数
     * @param <T>        原始类型
     * @param <K>        键类型
     * @param <V>        值类型
     */
    public static <T, K, V> Map<K, List<V>> listGroup(@Nullable Collection<T> collection, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (nonEmpty(collection)) {
            Map<K, List<V>> targetMap = new HashMap<>();
            Map<K, List<T>> middleMap = collection.stream().collect(Collectors.groupingBy(keyFunc));
            middleMap.forEach((sk, sv) -> targetMap.put(sk, sv.stream().map(valueFunc).collect(Collectors.toList())));
            return targetMap;
        }
        return new HashMap<>(0, 1);
    }

    /**
     * 如果集合不为空则映射为新的类型
     *
     * @param collection 原始集合
     * @param mapper     映射函数
     * @param <T>        原始泛型
     * @param <U>        新泛型
     * @return 将原始类型按mapper的方式转换为新类型，与map不同之处为它的映射器参数是集合本身，而不是集合内的元素
     */
    public static <T, U> List<U> wholeMap(@Nullable Collection<T> collection, Function<Collection<T>, List<U>> mapper) {
        if (nonEmpty(collection)) {
            return mapper.apply(collection);
        }
        return new ArrayList<>(0);
    }

    /**
     * 判断集合是否为空，如果为空则执行函数
     *
     * @param collection 集合
     * @param runnable   集合为空执行的函数
     * @return 集合是否为空
     */
    public static boolean isEmpty(@Nullable Collection<?> collection, Runnable runnable) {
        boolean empty = collection == null || collection.isEmpty();
        if (empty) {
            runnable.run();
        }
        return empty;
    }


    /**
     * 判断集合是否为空，如果为空则执行函数
     * @return 集合是否为空
     */
    public static boolean isEmpty(@Nullable Object[] args) {
        return args == null || args.length == 0;
    }

    /**
     * 集合转Stream
     *
     * @param collection 原始集合
     * @return 如果 collection == null 会返回 Stream.empty() 否则返回 collection.stream()
     */
    public static <T> Stream<T> toStream(@Nullable Collection<T> collection) {
        if (collection == null) {
            return Stream.empty();
        }
        return collection.stream();
    }
    /**
     * 集合转Stream并行流
     *
     * @param collection 原始集合
     * @return 如果 collection == null 会返回 Stream.empty() 否则返回 collection.stream()
     */
    public static <T> Stream<T> toParallelStream(@Nullable Collection<T> collection) {
        if (collection == null) {
            return Stream.empty();
        }
        return collection.parallelStream();
    }


    /**
     * 集合转Stream
     *
     * @param collection 原始集合
     * @return 如果 collection == null 会返回 Stream.empty() 否则返回 collection.stream()
     */
    public static <T> Stream<T> toStream(@Nullable T[] collection) {
        return isEmpty(collection) ? Stream.empty() : Arrays.stream(collection);
    }

    /**
     * 集合转Set
     *
     * @param collection 原始集合
     */
    public static <T> Set<T> toSet(@Nullable Collection<T> collection) {
        if (collection instanceof Set) {
            return (Set<T>) collection;
        }
        HashSet<T> hashSet = new HashSet<>();
        safeEach(collection, hashSet::add);
        return hashSet;
    }

    /**
     * 请参阅{@link Arrays#asList(Object[])}} 不同的是这里返回可变的集合 {@link ArrayList}
     */
    @SafeVarargs
    public static <T> List<T> asList(T... a) {
        ArrayList<T> list = new ArrayList<>();
        if (a != null) {
            Collections.addAll(list, a);
        }
        return list;
    }


    /**
     * 请参阅{@link #asList(Object[])} 元素会经过predicate过滤后加入集合
     *
     * @param predicate 加入集合的条件过滤器
     */
    @SafeVarargs
    public static <T> List<T> asList(Predicate<T> predicate, T... a) {
        ArrayList<T> list = new ArrayList<>();
        if (a != null) {
            Arrays.stream(a).filter(predicate).forEach(list::add);
        }
        return list;
    }
}
