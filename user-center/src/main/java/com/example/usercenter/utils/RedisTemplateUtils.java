package com.example.usercenter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisTemplate 工具类
 * 提供 Redis 常用操作命令
 *
 * 注意：
 * 为保证兼容性、避免隐患，value 均使用字符串格式进行存储
 * 请以字符串序列化器操作 RedisTemplate
 *
 * 包含：
 * 基本操作 {@link RedisTemplate}
 * 字符串操作 {@link ValueOperations}
 * 哈希操作 {@link HashOperations}
 * 列表操作 {@link ListOperations}
 * 集合操作 {@link SetOperations}
 * 有序集合操作 {@link ZSetOperations}
 */
@Slf4j
public class RedisTemplateUtils {

    // ---------------------------通用操作---------------------------

    /**
     * 判断键是否存在
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 执行结果：true=存在、false=不存在
     */
    public static Boolean hasKey(RedisTemplate redisTemplate, String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 统计存在键的数量
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @return 存在键的数量
     */
    public static Long countExistingKeys(RedisTemplate redisTemplate, Collection<String> keys) {
        return redisTemplate.countExistingKeys(keys);
    }

    /**
     * 删除键
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 执行结果：true=删除成功、false=删除失败
     */
    public static Boolean delete(RedisTemplate redisTemplate, String key) {
        return redisTemplate.delete(key);

    }

    /**
     * 批量删除键
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @return 被删除键的数量
     */
    public static Long delete(RedisTemplate redisTemplate, Collection keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 设置键的过期时间
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param timeout 时间单位
     * @param unit 过期时间
     * @return 执行结果：true=设置成功、false=设置失败
     */
    public static Boolean expire(RedisTemplate redisTemplate, String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置键的过期时间
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param date 缓存截止日期
     * @return 执行结果：true=设置成功、false=设置失败
     */
    public static Boolean expireAt(RedisTemplate redisTemplate, String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 获取键的过期时间
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param timeUnit 时间单位
     * @return 过期时间
     */
    public static Long getExpire(RedisTemplate redisTemplate, String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 删除键的过期时间
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 执行结果：true=删除成功、false=删除失败
     */
    public Boolean persist(RedisTemplate redisTemplate, String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 修改键名称
     * @param redisTemplate RedisTemplate 实例
     * @param oldKey 旧键
     * @param newKey 新键
     */
    public static void rename(RedisTemplate redisTemplate, String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 判断键是否存在并修改键名称
     * @param redisTemplate RedisTemplate 实例
     * @param oldKey 旧键
     * @param newKey 新键
     * @return 执行结果：执行结果：true=修改成功、false=修改失败
     */
    public static Boolean renameIfAbsent(RedisTemplate redisTemplate, String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    // ---------------------------字符串操作---------------------------

    /**
     * 设置键值对
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     */
    public static void set(RedisTemplate redisTemplate, String key, String value) {
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    /**
     * 设置键值对，同时设置过期时间
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public static void set(RedisTemplate redisTemplate, String key, String value, long timeout, TimeUnit unit) {
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout, unit);
    }

    /**
     * 键不存在时设置
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @return 执行结果：true=设置成功、false=设置失败
     */
    public static Boolean setIfAbsent(RedisTemplate redisTemplate, String key, String value) {
        ValueOperations operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(key, value);
    }

    /**
     * 键不存在时设置，并设置过期时间
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 执行结果：true=设置成功、false=设置失败
     */
    public static Boolean setIfAbsent(RedisTemplate redisTemplate, String key, String value, long timeout, TimeUnit unit) {
        ValueOperations operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 批量设置键值对
     * @param redisTemplate RedisTemplate 实例
     * @param map 多个键值对
     */
    public static void multiSet(RedisTemplate redisTemplate, Map<String, String> map) {
        ValueOperations operations = redisTemplate.opsForValue();
        operations.multiSet(map);
    }

    /**
     * 键不存在时批量设置键值对
     * 若多个键值对中有一个存在，则返回 false。
     * @param redisTemplate RedisTemplate 实例
     * @param map 多个键值对
     */
    public static Boolean multiSetIfAbsent(RedisTemplate redisTemplate, Map<String, String> map) {
        ValueOperations operations = redisTemplate.opsForValue();
        return operations.multiSetIfAbsent(map);
    }

    /**
     * 若 key 存在在末尾追加字符串，否则创建一个 value 为空串的 key（此时类似于 set key ""）
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 追加的字符串
     * @return 字符串长度
     */
    public static Integer append(RedisTemplate redisTemplate, String key, String value) {
        ValueOperations operations = redisTemplate.opsForValue();
        return operations.append(key, value);
    }

    /**
     * 获取指定键的值
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 对应的值
     */
    public static String get(RedisTemplate redisTemplate, String key) {
        ValueOperations operations = redisTemplate.opsForValue();
        return (String) operations.get(key);
    }

    /**
     * 获取多个键的值
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @return 对应的值集合
     */
    public static List<String> multiGet(RedisTemplate redisTemplate, Collection<String> keys) {
        ValueOperations operations = redisTemplate.opsForValue();
        return operations.multiGet(keys);
    }

    /**
     * 对字符串类型的数字值增 1
     * key 不存在时，会先赋初值为 0，再进行自增操作；当值不能表示为数字时，报错
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 自增后的值
     */
    public static Long increment(RedisTemplate redisTemplate, String key) {
        ValueOperations operations = redisTemplate.opsForValue();
        return operations.increment(key);
    }

    /**
     * 对字符串类型的数字值进行整数增量
     * key 不存在时，会先赋初值为 0，再进行自增操作；当值不能表示为数字时，报错
     * 支持符号，正值增量，负值减量
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param increment 增量
     * @return 增量后的值
     */
    public static Long increment(RedisTemplate redisTemplate, String key, long increment) {
        ValueOperations operations = redisTemplate.opsForValue();
        return operations.increment(key, increment);
    }

    /**
     * 对字符串类型的数字值进行浮点增量
     * key 不存在时，会先赋初值为 0，再进行自增操作；当值不能表示为数字时，报错
     * 支持符号，正值增量，负值减量
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param increment 增量
     * @return 增量后的值
     */
    public static Double increment(RedisTemplate redisTemplate, String key, double increment) {
        ValueOperations operations = redisTemplate.opsForValue();
        return operations.increment(key, increment);
    }

    // ---------------------------哈希操作---------------------------

    /**
     * 判断哈希表中键是否存在
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param hashKey 哈希键
     * @return 执行结果：true=存在、false=不存在
     */
    public static Boolean hasKey(RedisTemplate redisTemplate, String key, String hashKey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.hasKey(key, hashKey);
    }

    /**
     * 删除哈希表中的键值对
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param hashKeys 哈希键
     * @return 删除键值对的数量
     */
    public static Long delete(RedisTemplate redisTemplate, String key, String... hashKeys) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.delete(key, hashKeys);
    }

    /**
     * 设置哈希表键值对
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param hashKey 哈希键
     * @param value 值
     */
    public static void put(RedisTemplate redisTemplate, String key, String hashKey, String value) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, hashKey, value);
    }

    /**
     * 批量设置哈希表键值对
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param map 键值对 map
     */
    public static void putAll(RedisTemplate redisTemplate, String key, Map<String, String> map) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, map);
    }

    /**
     * 哈希表中哈希键不存在则添加
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param hashKey 哈希键
     * @param value 值
     * @return 执行结果：true=添加成功、false=添加失败
     */
    public static Boolean putIfAbsent(RedisTemplate redisTemplate, String key, String hashKey, String value) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.putIfAbsent(key, hashKey, value);
    }

    /**
     * 获取哈希表中指定键的值的长度
     * @param redisTemplate
     * @param key
     * @param hashKey
     * @return
     */
    public static Long lengthOfValue(RedisTemplate redisTemplate, String key, String hashKey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.lengthOfValue(key, hashKey);
    }

    /**
     * 获取哈希表中指定键的值
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param hashKey 哈希键
     * @return 对应的值
     */
    public static String get(RedisTemplate redisTemplate, String key, String hashKey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return (String) hashOperations.get(key, hashKey);
    }

    /**
     * 获取哈希表中多个键的值
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param hashKeys 哈希键集合
     * @return 对应的值集合
     */
    public static List<String> multiGet(RedisTemplate redisTemplate, String key, Collection<String> hashKeys) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.multiGet(key, hashKeys);
    }

    /**
     * 获取哈希表的大小
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 哈希表大小
     */
    public static Long size(RedisTemplate redisTemplate, String key) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.size(key);
    }

    /**
     * 获取哈希表中所有的哈希键
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 哈希键集合
     */
    public static Set<String> keys(RedisTemplate redisTemplate, String key) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.keys(key);
    }

    /**
     * 获取哈希表中所有哈希键的值集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 哈希键的值集合
     */
    public static List<String> values(RedisTemplate redisTemplate, String key) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(key);
    }

    /**
     * 获取哈希中指定键的所有键值对
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 键值对 map
     */
    public static Map<String, String> entries(RedisTemplate redisTemplate, String key) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    /**
     * 对哈希中数字值进行整数增量
     * key 不存在时，会先赋初值为 0，再进行自增操作；当值不能表示为数字时，报错
     * 支持符号，正值增量，负值减量
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param hashKey 哈希键
     * @param increment 增量
     * @return 增量后的值
     */
    public static Long increment(RedisTemplate redisTemplate, String key, String hashKey, long increment) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.increment(key, hashKey, increment);
    }

    /**
     * 对哈希中数字值进行浮点增量
     * key 不存在时，会先赋初值为 0，再进行自增操作；当值不能表示为数字时，报错
     * 支持符号，正值增量，负值减量
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param hashKey 哈希键
     * @param increment 增量
     * @return 增量后的值
     */
    public static Double increment(RedisTemplate redisTemplate, String key, String hashKey, double increment) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.increment(key, hashKey, increment);
    }

    // ---------------------------列表操作---------------------------

    /**
     * 获取列表的大小
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 列表大小
     */
    public static Long sizeOfList(RedisTemplate redisTemplate, String key) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.size(key);
    }

    /**
     * 向列表的头部插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @return 列表大小
     */
    public static Long leftPush(RedisTemplate redisTemplate, String key, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.leftPush(key, value);
    }

    /**
     * 向列表首个目标元素的左边插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param target 目标元素
     * @param value 值
     * @return 列表大小
     */
    public static Long leftPush(RedisTemplate redisTemplate, String key, String target, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.leftPush(key, target, value);
    }

    /**
     * 按顺序批量向列表的头部插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param values 值数组
     * @return 列表大小
     */
    public static Long leftPushAll(RedisTemplate redisTemplate, String key, String... values) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.leftPushAll(key, values);
    }

    /**
     * 按顺序批量向列表的头部插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param values 值集合
     * @return 列表大小
     */
    public static Long leftPushAll(RedisTemplate redisTemplate, String key, Collection<String> values) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.leftPushAll(key, values);
    }

    /**
     * 当列表存在时向列表的头部插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @return 列表大小，0=不存在
     */
    public static Long leftPushIfPresent(RedisTemplate redisTemplate, String key, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.leftPushIfPresent(key, value);
    }

    /**
     * 向列表的尾部插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @return 列表大小
     */
    public static Long rightPush(RedisTemplate redisTemplate, String key, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.rightPush(key, value);
    }

    /**
     * 向列表首个目标元素的右边插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param target 目标元素
     * @param value 值
     * @return 列表大小
     */
    public static Long rightPush(RedisTemplate redisTemplate, String key, String target, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.rightPush(key, target, value);
    }

    /**
     * 按顺序批量向列表的尾部插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param values 值数组
     * @return 列表大小
     */
    public static Long rightPushAll(RedisTemplate redisTemplate, String key, String... values) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.rightPushAll(key, values);
    }

    /**
     * 按顺序批量向列表的尾部插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param values 值集合
     * @return 列表大小
     */
    public static Long rightPushAll(RedisTemplate redisTemplate, String key, Collection<String> values) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.leftPushAll(key, values);
    }

    /**
     * 当列表存在时向列表的尾部插入元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @return 列表大小，0=不存在
     */
    public static Long rightPushIfPresent(RedisTemplate redisTemplate, String key, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.rightPushIfPresent(key, value);
    }

    /**
     * 根据索引修改列表的值
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    public static void set(RedisTemplate redisTemplate, String key, long index, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.set(key, index, value);
    }

    /**
     * 删除列表中等于目标值的元素
     * type > 0，从头部开始删除第一个值等于目标值的元素
     * type = 0，删除所有等于目标值的元素
     * type < 0，从尾部开始删除第一个值等于目标值的元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param type 删除类型
     * @param value 值
     * @return 删除元素的数量
     */
    public static Long remove(RedisTemplate redisTemplate, String key, long type, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.remove(key, type, value);
    }

    /**
     * 删除列表指定区间之外的元素集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param start 区间开始索引
     * @param end 区间结束索引
     */
    public static void trim(RedisTemplate redisTemplate, String key, long start, long end) {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.trim(key, start, end);
    }

    /**
     * 获取从列表头部开始第一个值匹配元素的索引
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @return 索引
     */
    public static Long indexOf(RedisTemplate redisTemplate, String key, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.indexOf(key, value);
    }

    /**
     * 获取从列表尾部开始第一个值匹配元素的索引
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @return 索引
     */
    public static Long lastIndexOf(RedisTemplate redisTemplate, String key, String value) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.lastIndexOf(key, value);
    }

    /**
     * 根据键和索引获取列表元素值
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param index 索引
     * @return 值
     */
    public static String index(RedisTemplate redisTemplate, String key, long index) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.index(key, index);
    }

    /**
     * 获取列表指定区间的元素集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param start 区间开始索引
     * @param end 区间结束索引
     * @return 元素集合
     */
    public static List<String> range(RedisTemplate redisTemplate, String key, long start, long end) {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations.range(key, start, end);
    }

    /**
     * 移除并获取列表的第一个元素值
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 值
     */
    public static String leftPop(RedisTemplate redisTemplate, String key) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.leftPop(key);
    }

    /**
     * 移除并获取列表的第一个元素值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 值
     */
    public static String leftPop(RedisTemplate redisTemplate, String key, long timeout, TimeUnit unit) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.leftPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表的第一个元素值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param timeout 超时时间
     * @return 值
     */
    public static String leftPop(RedisTemplate redisTemplate, String key, Duration timeout) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.leftPop(key, timeout);
    }

    /**
     * 移除并获取列表的最后一个元素值
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 值
     */
    public static String rightPop(RedisTemplate redisTemplate, String key) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.rightPop(key);
    }

    /**
     * 移除并获取列表的最后一个元素值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 值
     */
    public static String rightPop(RedisTemplate redisTemplate, String key, long timeout, TimeUnit unit) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.rightPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表的最后一个元素值，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param timeout 超时时间
     * @return 值
     */
    public static String rightPop(RedisTemplate redisTemplate, String key, Duration timeout) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.rightPop(key, timeout);
    }

    /**
     * 移除并获取源列表的最后一个元素值，并将此元素加入目标列表的头部
     * @param redisTemplate RedisTemplate 实例
     * @param sourceKey 源列表键
     * @param destinationKey 目标列表键
     * @return 值
     */
    public static String rightPopAndLeftPush(RedisTemplate redisTemplate, String sourceKey, String destinationKey) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.rightPopAndLeftPush(sourceKey, destinationKey);
    }

    /**
     * 移除并获取源列表的最后一个元素值，并将此元素加入目标列表的头部，如果源列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param redisTemplate RedisTemplate 实例
     * @param sourceKey 源列表键
     * @param destinationKey 目标列表键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 值
     */
    public static String rightPopAndLeftPush(RedisTemplate redisTemplate, String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
    }

    /**
     * 移除并获取源列表的最后一个元素值，并将此元素加入目标列表的头部，如果源列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param redisTemplate RedisTemplate 实例
     * @param sourceKey 源列表键
     * @param destinationKey 目标列表键
     * @param timeout 超时时间
     * @return 值
     */
    public static String rightPopAndLeftPush(RedisTemplate redisTemplate, String sourceKey, String destinationKey, Duration timeout) {
        ListOperations listOperations = redisTemplate.opsForList();
        return (String) listOperations.rightPopAndLeftPush(sourceKey, destinationKey, timeout);
    }

    // ---------------------------集合操作---------------------------

    /**
     * 向集合中添加元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param values 需要添加的元素
     * @return 添加元素的数量
     */
    public Long add(RedisTemplate redisTemplate, String key, String... values) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.add(key, values);
    }

    /**
     * 删除集合中的元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param values 需要删除的元素
     * @return 删除元素的数量
     */
    public Long remove(RedisTemplate redisTemplate, String key, String... values) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.remove(key, values);
    }

    /**
     * 随机删除集合中的一个元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 被删除的元素
     */
    public String pop(RedisTemplate redisTemplate, String key) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return (String) setOperations.pop(key);
    }

    /**
     * 随机删除集合中的多个元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param count 被移除元素的数量
     * @return 被删除的元素列表
     */
    public List<String> pop(RedisTemplate redisTemplate, String key, long count) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.pop(key, count);
    }

    /**
     * 将指定元素从源集合移动到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param value 值
     * @param destKey 目标集合键
     * @return 执行结果：true=操作成功、false=操作失败
     */
    public static Boolean move(RedisTemplate redisTemplate, String key, String value, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 集合大小
     */
    public static Long sizeOfSet(RedisTemplate redisTemplate, String key) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.size(key);
    }

    /**
     * 判断集合中元素是否存在
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @return 执行结果：true=存在、false=不存在
     */
    public static Boolean isMember(RedisTemplate redisTemplate, String key, String value) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(key, value);
    }

    /**
     * 判断集合中多个元素是否存在
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值数组
     * @return Map<value, result>
     */
    public static Map<String, Boolean> isMember(RedisTemplate redisTemplate, String key, String... value) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(key, value);
    }

    /**
     * 获取源集合与目标集合元素交集
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKey 目标集合键
     * @return 元素交集
     */
    public static Set<String> intersect(RedisTemplate redisTemplate, String key, String otherKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.intersect(key, otherKey);
    }

    /**
     * 获取源集合与其他目标集合元素交集
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKeys 其他目标集合键
     * @return 元素交集
     */
    public static Set<String> intersect(RedisTemplate redisTemplate, String key, Collection<String> otherKeys) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.intersect(key, otherKeys);
    }

    /**
     * 获取多个集合元素交集
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @return 元素交集
     */
    public static Set<String> intersect(RedisTemplate redisTemplate, Collection<String> keys) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.intersect(keys);
    }

    /**
     * 将源集合与另一个集合的元素交集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKey 其他集合键
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long intersectAndStore(RedisTemplate redisTemplate, String key, String otherKey, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 将源集合与其他集合的元素交集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKeys 其他集合键
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long intersectAndStore(RedisTemplate redisTemplate, String key, Collection<String> otherKeys, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 将多个集合的元素交集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long intersectAndStore(RedisTemplate redisTemplate, Collection<String> keys, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.intersectAndStore(keys, destKey);
    }

    /**
     * 获取源集合与目标集合元素并集
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKey 目标集合键
     * @return 元素并集
     */
    public static Set<String> union(RedisTemplate redisTemplate, String key, String otherKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.union(key, otherKey);
    }

    /**
     * 获取源集合与其他目标集合元素并集
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKeys 其他目标集合键
     * @return 元素并集
     */
    public static Set<String> union(RedisTemplate redisTemplate, String key, Collection<String> otherKeys) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.union(key, otherKeys);
    }

    /**
     * 获取多个集合元素并集
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @return 元素并集
     */
    public static Set<String> union(RedisTemplate redisTemplate, Collection<String> keys) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.union(keys);
    }

    /**
     * 将源集合与另一个集合的元素并集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKey 其他集合键
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long unionAndStore(RedisTemplate redisTemplate, String key, String otherKey, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.unionAndStore(key, otherKey, destKey);
    }

    /**
     * 将源集合与其他集合的元素并集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKeys 其他集合键
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long unionAndStore(RedisTemplate redisTemplate, String key, Collection<String> otherKeys, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 将多个集合的元素并集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long unionAndStore(RedisTemplate redisTemplate, Collection<String> keys, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.unionAndStore(keys, destKey);
    }

    /**
     * 获取源集合与目标集合元素差集
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKey 目标集合键
     * @return 元素差集
     */
    public static Set<String> difference(RedisTemplate redisTemplate, String key, String otherKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.difference(key, otherKey);
    }

    /**
     * 获取源集合与其他目标集合元素差集
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKeys 其他目标集合键
     * @return 元素差集
     */
    public static Set<String> difference(RedisTemplate redisTemplate, String key, Collection<String> otherKeys) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.difference(key, otherKeys);
    }

    /**
     * 获取多个集合元素差集
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @return 元素差集
     */
    public static Set<String> difference(RedisTemplate redisTemplate, Collection<String> keys) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.difference(keys);
    }

    /**
     * 将源集合与另一个集合的元素差集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKey 其他集合键
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long differenceAndStore(RedisTemplate redisTemplate, String key, String otherKey, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.differenceAndStore(key, otherKey, destKey);
    }

    /**
     * 将源集合与其他集合的元素差集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param key 源集合键
     * @param otherKeys 其他集合键
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long differenceAndStore(RedisTemplate redisTemplate, String key, Collection<String> otherKeys, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * 将多个集合的元素差集存储到目标集合
     * @param redisTemplate RedisTemplate 实例
     * @param keys 键集合
     * @param destKey 目标集合键
     * @return 目标集合大小
     */
    public static Long differenceAndStore(RedisTemplate redisTemplate, Collection<String> keys, String destKey) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.differenceAndStore(keys, destKey);
    }

    /**
     * 获取集合元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 值集合
     */
    public static Set<String> members(RedisTemplate redisTemplate, String key) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.members(key);
    }

    /**
     * 随机获取一个集合元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @return 值
     */
    public static String randomMembers(RedisTemplate redisTemplate, String key) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return (String) setOperations.randomMember(key);
    }

    /**
     * 随机获取多个集合元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param count 元素数量
     * @return 值列表
     */
    public static List<String> randomMembers(RedisTemplate redisTemplate, String key, long count) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.randomMembers(key, count);
    }

    // --------------------------有序集合操作--------------------------

    /**
     * 向有序集合中添加元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @param score 分数
     * @return 执行结果：true=添加成功、false=添加失败
     */
    public static Boolean add(RedisTemplate redisTemplate, String key, String value, double score) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.add(key, value, score);
    }

    /**
     * 向有序集合中批量添加元素
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param tuples 元素集合
     * @return 添加元素的数量
     */
    public static Long add(RedisTemplate redisTemplate, String key, Set<ZSetOperations.TypedTuple<String>> tuples) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.add(key, tuples);
    }

    /**
     * 有序集合中元素不存在则添加
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param value 值
     * @param score 分数
     * @return 执行结果：true=添加成功、false=添加失败
     */
    public static Boolean addIfAbsent(RedisTemplate redisTemplate, String key, String value, double score) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.addIfAbsent(key, value, score);
    }

    /**
     * 有序集合中元素不存在则批量添加
     * @param redisTemplate RedisTemplate 实例
     * @param key 键
     * @param tuples 元素集合
     * @return 添加元素的数量
     */
    public static Long addIfAbsent(RedisTemplate redisTemplate, String key, Set<ZSetOperations.TypedTuple<String>> tuples) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.addIfAbsent(key, tuples);
    }

    // TODO
}
