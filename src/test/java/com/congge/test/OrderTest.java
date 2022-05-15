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
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        String key = "redisKey";
        RLock lock = redisson.getLock(key);
        lock.lock(30, TimeUnit.SECONDS);
        System.out.println("获取到了锁");
        try {
            System.out.println("开始执行业务逻辑");
            Thread.sleep(10000);
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
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
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
