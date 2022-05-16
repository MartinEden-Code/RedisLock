package com.congge.common.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * zookeeper 分布式锁客户端配置
 */
@Configuration
public class CuratorLock {

    /*
    curator 的几种锁方案 ：
        1、InterProcessMutex：分布式可重入排它锁
        2、InterProcessSemaphoreMutex：分布式排它锁
        3、InterProcessReadWriteLock：分布式读写锁
    */

    @Bean(initMethod = "start",destroyMethod = "close")
    public CuratorFramework getCuratorFramework(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        return client;
    }

}
