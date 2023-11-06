package com.tutorial.stringredistemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

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

}
