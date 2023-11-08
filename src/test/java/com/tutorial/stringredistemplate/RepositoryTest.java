package com.tutorial.stringredistemplate;

import com.tutorial.data.Product;
import com.tutorial.repository.ProductRedisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Map;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductRedisRepository productRedisRepository;

    /**
     * Repository
     * ● Saat kita belajar Spring Data JPA, salah satu fitur yang sangat menarik adalah fitur Repository
     * ● Dimana kita tidak perlu melakukan manual lagi manipulasi data menggunakan JPA, cukup menggunakan Repository
     * ● Spring Data Redis juga mendukung fitur Repository
     * ● Data yang disimpan di redis, akan disimpan dalam bentuk tipe data Hash
     *
     * Enable Redis Repositories
     * ● Untuk mengaktifkan fitur Repository, kita harus menggunakan annotation @EnableRedisRepositories pada Spring Boot Application
     * ● https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/repository/configuration/EnableRedisRepositories.html
     *
     * Entity
     * ● Untuk membuat Entity di Spring Data Redis, kita bisa menggunakan annotation @KeySpace
     * ● https://docs.spring.io/spring-data/keyvalue/docs/current/api/org/springframework/data/keyvalue/annotation/KeySpace.html
     * ● Selain itu, kita juga perlu menentukan attribute yang akan dijadikan sebagai @Id pada Entity tersebut
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/annotation/Id.html
     *
     * Key Value Repository
     * ● Untuk membuat Repository, kita bisa membuat interface turunan dari KeyValueRepository
     * ● https://docs.spring.io/spring-data/keyvalue/docs/current/api/org/springframework/data/keyvalue/repository/KeyValueRepository.html
     */

    @Test
    void testRepository(){

        Product indomieGoreng = Product.builder()
                .id("1")
                .name("indomie goreng")
                .price(3000L)
                .ttl(-1L)
                .build();
        productRedisRepository.save(indomieGoreng); // transaksi DB
        Assertions.assertNotNull(indomieGoreng);

        Map<Object, Object> map = redisTemplate.opsForHash().entries("products:1"); // Map collection as Hashes data structure.. hasil get Hashes full String
        Assertions.assertEquals(indomieGoreng.getId(), map.get("id"));
        Assertions.assertEquals(indomieGoreng.getName(), map.get("name"));
        Assertions.assertEquals(indomieGoreng.getPrice().toString(), map.get("price"));

        Product product = productRedisRepository.findById("1").get();
        Assertions.assertEquals( indomieGoreng, product); // cek apakah id 1 sama data yang di inject dengan data di dalam redis

        /**
         * redis result:
         * localhost:6379> hgetall "products:1"
         * 1) "_class"
         * 2) "com.tutorial.data.Product"
         * 3) "id"
         * 4) "1"
         * 5) "name"
         * 6) "indomie goreng"
         * 7) "price"
         * 8) "3000"
         * localhost:6379>
         */

    }

    /**
     * Entity Time to Live
     *   Saat membuat Entity, kadang-kadang kita ingin juga mengatur waktu expired nya
     *   Kita bisa menggunakan annotation @TimeToLive pada salah satu atribut yang bernilai number
     *   Secara otomatis Spring Data Redis akan menggunakan nilai di atribut @TimeToLive untuk menentukan berapa lama data harus dihapus di Redis
     */

    @Test
    void testRepositoryTTL() throws InterruptedException {

        Product indomieSoto = Product.builder()
                .id("1")
                .name("indomie soto")
                .price(3000L)
                .ttl(3L)
                .build();

        productRedisRepository.save(indomieSoto); // transaksi DB
        Assertions.assertTrue(productRedisRepository.findById("1").isPresent()); // cek apakah datanya ada

        Thread.sleep(Duration.ofSeconds(5));
        Assertions.assertFalse(productRedisRepository.findById("1").isPresent()); // cek apakah datanya sudah tidak ada karna waktu expire 3 detik

        /**
         * redis result:
         * localhost:6379> hgetall "products:1"
         * (empty array)
         * localhost:6379> exists "products:1"
         * (integer) 0
         * localhost:6379>
         */
    }

}
