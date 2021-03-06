package com.congge.test;

import com.congge.common.utils.ZkLock;
import com.congge.service.OrderService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 * 测试4种分布式锁
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testConcurrenOrder() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            service.execute(() -> {
                try {
                    cyclicBarrier.await();
                    orderService.createOrder();
                    System.out.println("产生订单");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                } finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        service.shutdown();
    }

    @Test
    public void testRedissionLock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        config.useSingleServer().setPassword("yuantu123");
        RedissonClient redisson = Redisson.create(config);
        String key = "redisKey";
        RLock lock = redisson.getLock(key);
        lock.lock(30, TimeUnit.SECONDS);
        System.out.println("获取到了锁");
        try {
            System.out.println("开始执行业务逻辑");
            Thread.sleep(1000);
            System.out.println("执行业务逻辑结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testZkLock() {
        ZkLock lock = new ZkLock();
        boolean info = lock.getLock("order");
        System.out.println("获取锁的结果是:" + info);
        lock.close();
    }

    @Test
    public void testCuratorLock() {
        //1 重试策略：初试时间为3s 重试3次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //2 通过工厂创建连接
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        /*
        curator 的几种锁方案 ：
            1、InterProcessMutex：分布式可重入排它锁
            2、InterProcessSemaphoreMutex：分布式排它锁
            3、InterProcessReadWriteLock：分布式读写锁
        */
        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try {
            if (lock.acquire(30, TimeUnit.SECONDS)) {
                try {
                    System.out.println("获取到了锁");
                } finally {
                    System.out.println("释放了了锁");
                    lock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
    }



}
