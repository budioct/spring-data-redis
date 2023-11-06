package com.tutorial.stringredistemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.time.Duration;

@SpringBootTest
public class RedisTest {

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

    /**
     * List Operation
     *  Untuk berinteraksi dengan struktur data List di Redis, kita bisa menggunakan ListOperations class
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/ListOperations.html
     */

    @Test
    void testListOperation(){

        ListOperations<String, String> operations = redisTemplate.opsForList();

        operations.rightPush("names", "budhi"); // Long rightPush(K key, V value) // Tambahkan value ke key. dari kanan ke kiri
        operations.rightPush("names", "oct");
        operations.rightPush("names", "malik");

//         Assertions.assertEquals("malik", operations.rightPop("names")); // V rightPop(K key) dari kanan // Menghapus dan mengembalikan elemen terakhir dalam daftar yang disimpan di key.
//         Assertions.assertEquals("oct", operations.rightPop("names"));
//         Assertions.assertEquals("budhi", operations.rightPop("names"));

        Assertions.assertEquals("budhi", operations.leftPop("names")); // V leftPop(K key) dari kiri // Menghapus dan mengembalikan elemen terakhir dalam daftar yang disimpan di key.
        Assertions.assertEquals("oct", operations.leftPop("names"));
        Assertions.assertEquals("malik", operations.leftPop("names"));

    }

    /**
     * Set Operation
     *  Untuk berinteraksi dengan struktur data Set di Redis, kita bisa menggunakan SetOperations class
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/SetOperations.html
     */

    @Test
    void testSetOperation(){
        SetOperations<String, String> operations = redisTemplate.opsForSet(); // untuk collection uniq

        operations.add("students", "budhi");
        operations.add("students", "oct");
        operations.add("students", "malik");
        operations.add("students", "budhi");
        operations.add("students", "oct");
        operations.add("students", "malik");

        Assertions.assertEquals(3,operations.members("students").size()); // Set<V> members(K key) // get data dari Set operation
        assertThat(operations.members("students"), hasItems("budhi", "oct", "malik")); // void assertThat(T actual, Matcher<? super T> matcher)

        redisTemplate.delete("students");

    }


}
