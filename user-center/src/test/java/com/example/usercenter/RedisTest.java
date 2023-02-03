package com.example.usercenter;

import com.example.common.utils.JacksonUtils;
import com.example.usercenter.utils.RedisTemplateUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisTest extends BaseTest{

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStr() {
        String key = "str1";
        User user = new User()
                .setUserId(1L)
                .setUserName("test");

        Boolean result = redisTemplate.hasKey(key);
        System.out.println("key是否存在：" + result);

        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key, JacksonUtils.toString(user));
        System.out.println("信息存入redis");

        String s = (String) redisTemplate.opsForValue().get(user.toString());
        System.out.println("redis获取信息：" + s);
    }

    @Test
    public void testHash() {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        typedTuples.add(new DefaultTypedTuple<>("170", 170.0d));
//        typedTuples.add(new DefaultTypedTuple<>("100", 100.0d));
//        typedTuples.add(new DefaultTypedTuple<>("50", 50.0d));
//        Long result = RedisTemplateUtils.add(redisTemplate, "zset1", typedTuples);
        Boolean aBoolean = RedisTemplateUtils.addIfAbsent(redisTemplate, "zset3", "170", 50);
        System.out.println(aBoolean);
//        Long zset1 = RedisTemplateUtils.addIfAbsent(redisTemplate, "zset2", typedTuples);
//        System.out.println(zset1);
    }

    @Data
    @Accessors(chain = true)
    static class User {
        private Long userId;
        private String userName;
    }
}
