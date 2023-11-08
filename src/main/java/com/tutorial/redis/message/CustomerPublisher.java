package com.tutorial.redis.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class CustomerPublisher {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS) // kirimkan setiap 10s
    public void publisher(){

//        Parameter:
//        channel- saluran tujuan publikasi, tidak boleh nol.
//        message- pesan untuk dipublikasikan.
//        Pengembalian:
//        jumlah klien yang menerima pesan. null saat digunakan dalam pipeline/transaksi.
        redisTemplate.convertAndSend("customers", "budhi " + UUID.randomUUID().toString()); // Long convertAndSend(String channel, Object message) // Publikasikan pesan tertentu ke saluran tertentu.
    }

    /**
     * 2023-11-08T12:01:13.237+07:00  WARN 16904 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-08T12:01:18.161+07:00  INFO 16904 --- [enerContainer-2] c.t.redis.message.CustomerListener       : Received message: budhi 877f983e-1c31-4038-a597-3f22b4d951fb
     * 2023-11-08T12:01:23.185+07:00  WARN 16904 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-08T12:01:28.151+07:00  INFO 16904 --- [enerContainer-3] c.t.redis.message.CustomerListener       : Received message: budhi 95ac11bd-8428-49fc-a183-143c80f8a5ad
     * 2023-11-08T12:01:33.163+07:00  WARN 16904 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-08T12:01:38.158+07:00  INFO 16904 --- [enerContainer-4] c.t.redis.message.CustomerListener       : Received message: budhi 7fb7485e-ba85-4adf-a4cf-7cf5e6b77015
     * 2023-11-08T12:01:40.761+07:00  WARN 16904 --- [cTaskExecutor-1] io.lettuce.core.RedisChannelHandler      : Connection is already closed
     * 2023-11-08T12:01:40.762+07:00  WARN 16904 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis exception
     */

}
