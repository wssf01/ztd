package com.bike.ztd.util;

import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.redis.connection.RedisStringCommands.SetOption.ifAbsent;

public class RedisUtils {

    private static RedisTemplate<String, Object> redisBiz = BeanUtils.getBean("redisTemplate");

    private static ValueOperations<String, Object> codeVO = redisBiz.opsForValue();

    private static SetOperations<String, Object> opsForSet = redisBiz.opsForSet();

    public static void opsForSetValue(String key, Object value) {
        opsForSet.add(key, value);
    }

    public static Long opsForSetSize(String key) {
        return opsForSet.size(key);
    }

    public static Long increment(String key, long delta) {
        return codeVO.increment(key, delta);
    }

    public static Long getIncrValue(final String key) {
        return redisBiz.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisBiz.getStringSerializer();
            byte[] rowkey = serializer.serialize(key);
            byte[] rowval = connection.get(rowkey);
            try {
                String val = serializer.deserialize(rowval);
                return Long.parseLong(val);
            } catch (Exception e) {
                return 0L;
            }
        });
    }

    public static String getRDLockValue(final String key) {
        if (key == null) {
            return null;
        }
        return redisBiz.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = redisBiz.getStringSerializer();
            byte[] bytes = serializer.serialize(key);
            if (bytes == null) {
                return null;
            }
            byte[] rowVal = connection.get(bytes);
            try {
                return serializer.deserialize(rowVal);
            } catch (Exception e) {
                return null;
            }
        });
    }

    /**
     * redis分布式锁，
     * nx+expire实现
     *
     * @param keyStr
     * @param valueStr
     * @param milliseconds 单位：毫秒
     * @return
     */
    public static Boolean RDLock(final String keyStr, final String valueStr, final long milliseconds) {
        try {
            return redisBiz.execute((RedisCallback<Boolean>) redisConnection -> {
                RedisSerializer<String> redisSerializer = redisBiz.getStringSerializer();
                byte[] key = redisSerializer.serialize(keyStr);
                byte[] value = redisSerializer.serialize(valueStr);
                return redisConnection.set(key, value, Expiration.milliseconds(milliseconds), ifAbsent());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean setNX(final String keyStr, final String valueStr) {
        return redisBiz.execute((RedisCallback<Boolean>) redisConnection -> {
            RedisSerializer<String> redisSerializer = redisBiz.getStringSerializer();
            byte[] key = redisSerializer.serialize(keyStr);
            byte[] value = redisSerializer.serialize(valueStr);
            return redisConnection.setNX(key, value);
        });
    }

    /**
     * 获取
     *
     * @param key
     */
    public static void delete(String key) {
        redisBiz.delete(key);
    }

    /**
     * 批量清空记录.
     *
     * @param keys redis key 集合
     */
    public static void delete(Collection<String> keys) {
        if (keys == null) {
            return;
        }
        redisBiz.delete(keys);
    }

    /**
     * 批量清空记录.
     *
     * @param key redis key*匹配 集合
     */
    public static void deleteAll(String key) {
        Set<String> keys = keys(key + "*");
        if (keys == null) {
            return;
        }
        redisBiz.delete(keys);
    }

    /**
     * 设置有效时长
     *
     * @param key
     * @param mills
     */
    public static void expire(String key, long mills) {
        redisBiz.expire(key, mills, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置
     *
     * @param key
     * @param value
     * @param mills
     */
    public static void set(String key, byte[] value, long mills) {
        codeVO.set(key, value, mills, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置
     *
     * @param key
     * @param value
     * @param mills
     */
    public static void set(String key, Serializable value, long mills) {
        codeVO.set(key, value, mills, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置
     *
     * @param key
     * @param value
     */
    public static void set(String key, Serializable value) {
        codeVO.set(key, value);
    }

    /**
     * 获取
     *
     * @param key
     */
    public static Object get(String key) {
        return codeVO.get(key);
    }

    /**
     * 获取
     *
     * @param key
     */
    public static Set<String> keys(String key) {
        return redisBiz.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(key).count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });
    }

}
