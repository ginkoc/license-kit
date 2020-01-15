package com.ginko.license.manager.utils;

import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author ginko
 * @date 8/21/19
 */
public class RedisUtil {

    private static RedisTemplate<String, Serializable> template = SpringBeanUtil.getBean("customRedisTemplate");

    /**
     * 获取key过期时间
     *
     * @param key 键不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpireInSeconds(String key) {
        assert key != null;
        return template.getExpire(key, TimeUnit.SECONDS);

    }

    /**
     * 普通缓存放入
     *
     * @param key              键
     * @param value            值
     * @param timeoutInSeconds 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false失败
     */
    public static void set(String key, Serializable value, long timeoutInSeconds) {
        assert key != null;
        assert timeoutInSeconds > 0;
        template.opsForValue().set(key, value, timeoutInSeconds, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Serializable get(String key) {
        return key == null ? null : template.opsForValue().get(key);
    }
}
