package com.congge;

import org.redisson.api.RedissonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LockApp {

    public static void main(String[] args) {
        SpringApplication.run(LockApp.class,args);
    }

}
