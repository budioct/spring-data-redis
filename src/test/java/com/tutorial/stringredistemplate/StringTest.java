package com.tutorial.stringredistemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StringTest {

    /**
     * Redis Template
     *  Saat kita menggunakan Spring Data Redis, secara otomatis Spring Boot akan membuat sebuah bean dengan type RedisTemplate
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/RedisTemplate.html
     *  Redis Template merupakan tipe data generic, dan salah satu implementasinya yang biasa digunakan adalah StringRedisTemplate
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/StringRedisTemplate.html
     */

    @Autowired
    private StringRedisTemplate redisTemplate; // extends RedisTemplate<String, String> // object implement Resis Template

    @Test
    void testRedisTemplate(){

        Assertions.assertNotNull(redisTemplate);

    }

    /**
     * Value Operation
     *  Struktur data yang biasa digunakan saat kita menggunakan Redis adalah String
     *  Dimana kita bisa menggunakan Key-Value berupa String di Redis
     *  Untuk berinteraksi dengan struktur data String, kita bisa menggunakan ValueOperations class
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/ValueOperations.html
     */

    @Test
    void testValueOpertaion() throws InterruptedException {

        ValueOperations<String, String> oprations = redisTemplate.opsForValue(); // ValueOperations<K, V> opsForValue() // implement StringRedisTemplate

        oprations.set("budhi", "budhi", Duration.ofSeconds(2L)); // void set(K key, V value, Duration timeout) // set key value dengan time expire di redis
        Assertions.assertNotNull(oprations);
        Assertions.assertEquals("budhi", oprations.get("budhi")); // V get(Object key) // cek value pada key redis

        Thread.sleep(Duration.ofSeconds(3L));
        Assertions.assertNull(oprations.get("budhi"));

    }


}
