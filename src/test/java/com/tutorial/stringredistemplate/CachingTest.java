package com.tutorial.stringredistemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

@SpringBootTest
public class CachingTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    CacheManager cacheManager;

    /**
     * Spring Caching // (menyimpan data di memory secara sementara (Caching))
     *   Spring memiliki fitur bernama Caching, fitur ini digunakan untuk menyimpan data di memory secara sementara (Cache)
     *   Fitur Spring Caching, bisa di integrasikan dengan Spring Data Redis, sehingga data Cache bisa disimpan di Redis secara otomatis
     *   Untuk mengaktifkan fitur Caching, kita harus menggunakan annotation @EnableCaching
     *   https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/EnableCaching.html
     *
     *   Note:
     *   spring.cache.type=redis
     *   spring.cache.type=caffeine
     *   spring.cache.type=hazelcast
     *   kenapa kita menggunakan Caching, karena kalau kita mau ubah type data selain redis kita tidak perlu lagi harus merubah code yang sudah di buat (buat jaga jaga)
     *
     *   Cache Manager
     *    Spring Caching menggunakan class Cache Manager sebagai Cache Management nya
     *    https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/CacheManager.html
     *    Untuk memanipulasi data di Cache, kita bisa menggunakan class Cache
     *    https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/Cache.html
     *    Saat kita menambah Spring Data Redis, secara otomatis akan meregistrasikanRedisCacheManager sebagai implementasi dari Cache Manager
     *    https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/cache/RedisCacheManager.html
     */

    @Test
    void testSortedSetOperation(){

        ZSetOperations<String, String> operations = redisTemplate.opsForZSet(); // SortedSet collection.. hasil dari Set akan di sorted / urutkan

        operations.add("score", "oct", 90);
        operations.add("score", "budhi", 100);
        operations.add("score", "malik", 80);

        Assertions.assertEquals("budhi", operations.popMax("score").getValue()); // TypedTuple<V> popMax(K key) // Hapus dan kembalikan nilai dengan skornya yang memiliki skor tertinggi dari kumpulan yang diurutkan pada key.
        Assertions.assertEquals(90, operations.popMax("score").getScore()); // Double getScore() // mendapatkan score
        Assertions.assertEquals("malik", operations.popMax("score").getValue()); // V getValue() // mendapatkan value

        redisTemplate.delete("score");

    }

    @Test
    void testCaching(){

        Cache cache = cacheManager.getCache("scores"); // instance.. key -> cache:scores:budhi
        cache.put("budhi", 90); // void put(Object key, @Nullable Object value) // Kaitkan nilai yang ditentukan dengan kunci yang ditentukan dalam cache ini.
        cache.put("oct", 83);

        Assertions.assertNotNull(cache);
        Assertions.assertEquals(90, cache.get("budhi", Integer.class)); // <T> T get(Object key, @Nullable Class<T> type) // Mengembalikan nilai yang dipetakan oleh cache ini pada kunci yang ditentukan, secara umum menentukan jenis nilai yang dikembalikan yang akan digunakan.
        Assertions.assertEquals(83, cache.get("oct", Integer.class));

        cache.evict("budhi"); // void evict(Object key) // Hapus pemetaan kunci ini dari cache ini jika ada.
        cache.evict("oct");

        Assertions.assertNull(cache.get("budhi")); // ValueWrapper get(Object key) // Kembalikan nilai yang dipetakan oleh cache ini pada kunci yang ditentukan.
        Assertions.assertNull(cache.get("oct"));

    }

}
