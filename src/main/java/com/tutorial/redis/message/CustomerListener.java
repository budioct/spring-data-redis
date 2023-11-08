package com.tutorial.redis.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerListener implements MessageListener {

    /**
     * PubSub Listener
     *  Sama seperti Stream Listener, Spring Data Redis juga bisa membuat Listener untuk PubSub
     *  Caranya hampir sama, kita perlu membuat Message Listener Container
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/listener/RedisMessageListenerContainer.html
     *  Lalu Membuat Message Container
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/connection/MessageListener.html
     */

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String data = new String(message.getBody());
        log.info("Received message: {}", data);
    } // void onMessage() // Panggilan balik untuk memproses objek yang diterima melalui Redis.

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
