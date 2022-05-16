package com.congge.common.utils;

import com.congge.web.OrderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);


    private RedisTemplate redisTemplate;

    private String key;
    private String value;
    private int expireTime;

    public RedisLock(RedisTemplate redisTemplate,String key,int expireTime) {
        this.key = key;
        this.expireTime = expireTime;
        this.redisTemplate=redisTemplate;
        this.value = UUID.randomUUID().toString();
    }


    /**
     * 获取分布式锁
     *
     * @return
     */
    public boolean getLock() {
        RedisCallback<Boolean> redisCallback = connection -> {
            //设置NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            //设置过期时间
            Expiration expiration = Expiration.seconds(expireTime);
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            //执行set NX的操作
            Boolean result = connection.set(redisKey, redisValue, expiration, setOption);
            return result;
        };
        //获取分布式锁
        Boolean lock = (Boolean) redisTemplate.execute(redisCallback);
        return lock;
    }

    /**
     * 释放锁
     * @return
     */
    public boolean unLock() {
        Boolean result = false;
        //释放锁，利用lua脚本进行释放，下面这段代码可以从官网获取
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "\treturn redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "\treturn 0\n" +
                "end\t";
        RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
        List<String> keys = Arrays.asList(key);
        result = (Boolean) redisTemplate.execute(redisScript, keys, value);
        logger.info("释放锁,释放锁结果:{}", result);
        return result;
    }

}
