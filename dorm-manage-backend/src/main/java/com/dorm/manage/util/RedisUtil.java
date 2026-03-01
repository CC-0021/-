package com.dorm.manage.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类 - token 存储与限流
 *
 * @author dorm-manage
 */
@Component
public class RedisUtil {

    public static final String TOKEN_PREFIX = "token_";
    public static final String CAPTCHA_PREFIX = "captcha_";
    public static final String RATE_LIMIT_PREFIX = "rate:";
    /** token 过期时间 2 小时 */
    public static final long TOKEN_EXPIRE_HOURS = 2;
    /** 验证码过期 5 分钟 */
    public static final long CAPTCHA_EXPIRE_MINUTES = 5;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setToken(String username, String token) {
        stringRedisTemplate.opsForValue().set(TOKEN_PREFIX + username, token, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
    }

    public String getToken(String username) {
        return stringRedisTemplate.opsForValue().get(TOKEN_PREFIX + username);
    }

    public void deleteToken(String username) {
        stringRedisTemplate.delete(TOKEN_PREFIX + username);
    }

    public void setCaptcha(String key, String code) {
        stringRedisTemplate.opsForValue().set(CAPTCHA_PREFIX + key, code, CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    public String getCaptcha(String key) {
        return stringRedisTemplate.opsForValue().get(CAPTCHA_PREFIX + key);
    }

    /**
     * 限流：key 在 1 分钟内增加 1，超过 max 返回 false
     */
    public boolean tryIncrementRateLimit(String key, int maxPerMinute) {
        String k = RATE_LIMIT_PREFIX + key;
        Long count = stringRedisTemplate.opsForValue().increment(k);
        if (count != null && count == 1) {
            stringRedisTemplate.expire(k, 1, TimeUnit.MINUTES);
        }
        return count != null && count <= maxPerMinute;
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }
}
