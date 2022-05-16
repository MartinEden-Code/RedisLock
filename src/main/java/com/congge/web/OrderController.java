package com.congge.web;

import com.congge.common.ResponseResult;
import com.congge.common.utils.RedisLock;
import com.congge.common.utils.ZkLock;
import com.congge.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    private Lock lock = new ReentrantLock();

    @GetMapping("/order/get/{id}")
    public ResponseResult getOrderById(@PathVariable("id") String id) {
        return orderService.getOrderById(id);
    }

    //http://localhost:7748/order/create
    //http://localhost:7749/order/create
    @GetMapping("/order/create")
    public ResponseResult createOrder() throws Exception {
        return orderService.createOrder();
    }

    @GetMapping("/order/single/lock")
    public String singleLock() {
        logger.info("我进入了方法");
        lock.lock();
        logger.info("我进入了锁");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return "执行完毕";
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/testRedisLock")
    public String testRedisLock() {
        logger.info("进入方法");
        String key = "redisKey";
        String value = UUID.randomUUID().toString();
        RedisCallback<Boolean> redisCallback = connection -> {
            //设置NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            //设置过期时间
            Expiration expiration = Expiration.seconds(30);
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            //执行set NX的操作
            Boolean result = connection.set(redisKey, redisValue, expiration, setOption);
            return result;
        };
        //获取分布式锁
        Boolean lock = (Boolean) redisTemplate.execute(redisCallback);
        if (lock) {
            logger.info("我获取到了锁");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //释放锁，利用lua脚本进行释放，下面这段代码可以从官网获取
                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                        "\treturn redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "\treturn 0\n" +
                        "end\t";
                RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
                List<String> keys = Arrays.asList(key);
                Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
                logger.info("释放锁,释放锁结果:{}", result);
            }
        }
        logger.info("方法执行完成");
        return "方法执行完毕";
    }

    @GetMapping("/testGetRedisLock")
    public String testGetRedisLock() {
        logger.info("进入方法");
        String key = "redisKey";
        RedisLock redisLock = new RedisLock(redisTemplate, key, 30);
        if (redisLock.getLock()) {
            logger.info("我获取到了锁");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                boolean unLock = redisLock.unLock();
                logger.info("释放锁:{}", unLock);
            }
        }
        logger.info("方法执行完成");
        return "方法执行完毕";
    }

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/getRedissonLock")
    public String getRedissonLock() {
        logger.info("进入方法");
        String key = "redisKey";
        RLock lock = redissonClient.getLock(key);
        lock.lock();
        logger.info("获取到了锁");
        try {
            logger.info("开始执行业务逻辑");
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        logger.info("方法执行完成");
        return "方法执行完毕";
    }

    /*@GetMapping("/tryRedissonLock")
    public String tryRedissonLock() {
        logger.info("进入方法");
        String key = "redisKey";
        RLock lock = redissonClient.getLock(key);
        logger.info("获取到了锁");
        try {
            logger.info("开始执行业务逻辑");
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        logger.info("方法执行完成");
        return "方法执行完毕";
    }*/

    //http://localhost:7748/testZkLock
    //http://localhost:7749/testZkLock
    @GetMapping("/testZkLock")
    public String testZkLock() {
        logger.info("进入方法");
        ZkLock zkLock = new ZkLock();
        boolean info = zkLock.getLock("order");
        try {
            if (info) {
                logger.info("获取到了锁，开始执行耗时业务");
                Thread.sleep(10000);
                logger.info("方法执行完成，释放锁");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zkLock.close();
        return "success";
    }

    /**
     * zookeeper 分布式锁客户端
     */
    /*@Autowired
    private CuratorFramework zKclient;

    @GetMapping("/testCuratorLock")
    public String testCuratorLock() {
        logger.info("进入方法");
        InterProcessMutex lock = new InterProcessMutex(zKclient, "/order");
        try {
            if (lock.acquire(30, TimeUnit.SECONDS)) {
                logger.info("获取到了锁,开始执行耗时业务");
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("方法执行完成，释放锁");
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "success";
    }*/

}
