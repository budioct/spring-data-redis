package com.tutorial.stringredistemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.data.redis.support.collections.RedisZSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CollectionTest {

    /**
     * Java Collection
     *  Saat kita menggunakan Java, kita tahu bahwa hampir kebanyakan struktur data di Redis, itu sudah ada di Java, seperti java.util.List, java.util.Set, dan java.util.Map
     *  Spring Data Redis, memiliki fitur bisa melakukan konversi otomatis dari tipe data yang ada di Redis menjadi tipe data di Java
     *  Selain itu operasi yang kita lakukan di data tersebut, secara otomatis berdampak ke data yang ada di Redis nya secara otomatis
     *
     *  Spring Data Redis Collection
     *   Berikut adalah beberapa tipe data yang disediakan oleh Spring Data Redis agar bisa kompatibel dengan tipe data di Java Collection
     * Spring Data Redis Java Collection Redis Data Structure
     * RedisList        List        List
     * RedisSet         Set         Set
     * RedisZSet        Set         Sorted Set
     * RedisMap         Map         Hash
     *
     * Struktur Data di Redis
     * Struktur Data        Keterangan
     * Lists                Struktur data Linked List yang berisi data string
     * Sets                 Koleksi data string yang tidak berurut
     * Hashes               Struktur data key-value
     * Sorted               Sets Struktur data seperti Sets, namun berurut
     * Stream               Struktur data seperti log yang selalu bertambah dibelakang
     * Geospatial           Struktur data koordinat
     * HyperLogLog          Struktur data untuk melakukan estimasi kardinalitas dari Set
     */

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void testList(){

        // List
        // bisa polimorpsh // walaupun java.util.List ini bisa untuk list di redis
        List<String> list = RedisList.create("names", redisTemplate); // <E> RedisList<E> create(String key, RedisOperations<String, E> operations) // membat list pada redis dengan collection java

        list.add("budhi");
        list.add("oct");
        list.add("malik");
        assertThat(list, hasItems("budhi", "oct", "malik"));

        List<String> names = redisTemplate.opsForList().range("names", 0, -1);// List<V> range(K key, long start, long end) // akan get data list redis dengan set key, dan index berapa ke berapa // 0, -1 --> get index 0 sampai terakhir
        assertThat(names, hasItems("budhi", "oct", "malik"));

        /**
         * redis result:
         * localhost:6379> lrange names 0 -1
         * 1) "budhi"
         * 2) "oct"
         * 3) "malik"
         * localhost:6379>
         */

    }

    @Test
    void testSet(){

        // Set
        Set<String> set = RedisSet.create("traffic", redisTemplate);

        set.addAll(Set.of("budhi", "oct", "malik"));
        set.addAll(Set.of("budhi", "husein", "jamal"));
        assertThat(set, hasItems("budhi", "oct", "malik", "husein", "jamal"));

        Set<String> members = redisTemplate.opsForSet().members("traffic");
        assertThat(members, hasItems("budhi", "oct", "malik", "husein","jamal"));

        /**
         * redis result:
         * localhost:6379> smembers traffic
         * 1) "malik"
         * 2) "budhi"
         * 3) "oct"
         * 4) "husein"
         * 5) "jamal"
         * localhost:6379>
         */

    }

    @Test
    void testZSet(){

        // Sorted Set (karna ada nilai kita perlu menggunakan RedisZSet).. hasil adalah di sort dari angka yang terkecil sampai terbesar
        RedisZSet<String> set = RedisZSet.create("winner", redisTemplate);

        set.add("budhi", 90); // 4
        set.add("oct", 85); // 3
        set.add("malik", 80); // 1
        set.add("jamal", 83); // 2
        assertThat(set, hasItems("malik", "jamal", "oct", "budhi"));

        Set<String> winner = redisTemplate.opsForZSet().range("winner", 0, -1); // Set<V> range(K key, long start, long end) // instance dengan method range berdasarkan key start index sampai end index
        assertThat(winner, hasItems("malik", "jamal", "oct", "budhi"));

//        Assertions.assertEquals("budhi", set.popLast()); // E popLast() // get dari paling akhir
//        Assertions.assertEquals("oct", set.popLast());
//        Assertions.assertEquals("jamal", set.popLast());
//        Assertions.assertEquals("malik", set.popLast());

        Assertions.assertEquals("malik", set.popFirst()); // E popFirst() // get dari paling depan
        Assertions.assertEquals("jamal", set.popFirst());
        Assertions.assertEquals("oct", set.popFirst());
        Assertions.assertEquals("budhi", set.popFirst());

        /**
         * redis result:
         * localhost:6379> zrange winner 80 100 byscore.......... cek byscore start end
         * 1) "malik"
         * 2) "jamal"
         * 3) "oct"
         * 4) "budhi"
         * localhost:6379> zrange winner 0 -1.......... cek berdasarkan index start end
         * 1) "malik"
         * 2) "jamal"
         * 3) "oct"
         * 4) "budhi"
         * localhost:6379>
         */

    }

    @Test
    void testMap(){

        // Map<K,V> di java seperti Hashes redis key:value
        Map<String, String> map = new DefaultRedisMap<>("user:1", redisTemplate);

        map.put("name", "budhi");
        map.put("address", "tangerang");
        assertThat(map, hasEntry("name", "budhi")); //
        assertThat(map, hasEntry("address", "tangerang"));

        Map<Object, Object> user = redisTemplate.opsForHash().entries("user:1");
        assertThat(user, hasEntry("name", "budhi"));
        assertThat(user, hasEntry("address", "tangerang"));

        /**
         * redis result:
         * localhost:6379> hgetall "user:1"
         * 1) "name"
         * 2) "budhi"
         * 3) "address"
         * 4) "tangerang"
         * localhost:6379>
         */

    }




}
