package com.tutorial.stringredistemplate;

import com.tutorial.data.Product;
import com.tutorial.service.ProductRedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeclarativeCachingTest {

    /**
     * Declarative Caching
     *   Selain menggunakan Cache Manager, untuk melakukan manajemen data Cache, kita bisa menggunakan cara Declarative, yaitu menggunakan Annotation
     *   Jika sebuah method ditambahkan annotation untuk Caching, secara otomatis hasil dari method akan disimpan di Cahe
     *   Ada banyak annotation yang bisa kita gunakan untuk melakukan management Cache
     *
     * Cacheable
     *   Annotation @Cacheable Menandakan bahwa return value dari method harus disimpan di cache
     *   https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/Cacheable.html
     *   Secara default, RedisCacheManager akan menyimpan data dalam bentuk binary (byte[]), oleh karena itu kita perlu memastikan data Object nya implement Serializable agar bisa disimpan sebagai binary data
     *   https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html
     *
     * CachePut
     *   Annotation CachePut digunakan untuk mengubah data di Cache, tanpa harus mengakses method dengan annotation @Cacheable
     *   https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/CachePut.html
     *
     * CacheEvict
     *   Untuk menghapus data di Cache, selain secara otomatis menggunakan TTL, kita bisa menggunakan annotation @CacheEvict
     *   https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/CacheEvict.html
     */

    @Autowired
    ProductRedisService productRedisService;

    @Test
    void testCacheableGetData(){

        Product product = productRedisService.getProduct("P-001");

        Assertions.assertEquals("P-001", product.getId());
        Assertions.assertEquals("sample", product.getName());
        Assertions.assertEquals(100L, product.getPrice());

        Product product1 = productRedisService.getProduct("P-001");
        Assertions.assertEquals(product1, product);

        /**
         * redis result:
         * localhost:6379> keys *
         * 1) "orders"
         * 2) "cache:products::P-001"
         * localhost:6379> get "cache:products::P-001"
         * "\xac\xed\x00\x05sr\x00\x19com.tutorial.data.Product\xc2q\x88\x91\x9d\x0b\xd7\xb2\x02\x00\x04L\x00\x02idt\x00\x12Ljava/lang/String;L\x00\x04nameq\x00~\x00\x01L\x00\x05pricet\x00\x10Ljava/lang/Long;L\x00\x03ttlq\x00~\x00\x02xpt\x00\x05P-001t\x00\x06samplesr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x00\x00\x00\x00\x00dp"
         * localhost:6379>
         */

    }


    @Test
    void testCachePutData(){

        Product example = Product.builder()
                .id("P-002")
                .name("example")
                .price(100L)
                .build();
        productRedisService.putProduct(example);

        Product product = productRedisService.getProduct("P-002");

        Assertions.assertEquals(product, example);

        /**
         * redis result:
         * localhost:6379> keys *
         * 1) "orders"
         * 2) "cache:products::P-002"
         * localhost:6379> get "cache:products::P-002"
         * "\xac\xed\x00\x05sr\x00\x19com.tutorial.data.Product\xc2q\x88\x91\x9d\x0b\xd7\xb2\x02\x00\x04L\x00\x02idt\x00\x12Ljava/lang/String;L\x00\x04nameq\x00~\x00\x01L\x00\x05pricet\x00\x10Ljava/lang/Long;L\x00\x03ttlq\x00~\x00\x02xpt\x00\x05P-002t\x00\aexamplesr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x00\x00\x00\x00\x00dp"
         * localhost:6379>
         */

    }

    @Test
    void testRemoveData(){

        Product product = productRedisService.getProduct("P-003");
        Assertions.assertEquals("P-003", product.getId());

         productRedisService.removeProduct("P-003"); // jika kita tidak menghapus dia akan mengambil dari chache, tidak ambil dari connect DB

        Product product2 = productRedisService.getProduct("P-003");
        Assertions.assertEquals(product2, product);

        /**
         * result:
         * 2023-11-09T12:16:17.239+07:00  INFO 5168 --- [           main] c.tutorial.service.ProductRedisService   : Get Product: P-003
         * 2023-11-09T12:16:17.266+07:00  INFO 5168 --- [           main] c.tutorial.service.ProductRedisService   : Remove Product: P-003
         * 2023-11-09T12:16:17.274+07:00  INFO 5168 --- [           main] c.tutorial.service.ProductRedisService   : Get Product: P-003
         *
         * redis result:
         * localhost:6379> keys *
         * 1) "orders"
         * 2) "cache:products::P-003"
         * localhost:6379> get "cache:products::P-003"
         * "\xac\xed\x00\x05sr\x00\x19com.tutorial.data.Product\xc2q\x88\x91\x9d\x0b\xd7\xb2\x02\x00\x04L\x00\x02idt\x00\x12Ljava/lang/String;L\x00\x04nameq\x00~\x00\x01L\x00\x05pricet\x00\x10Ljava/lang/Long;L\x00\x03ttlq\x00~\x00\x02xpt\x00\x05P-003t\x00\x06samplesr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x00\x00\x00\x00\x00dp"
         * localhost:6379>
         */

    }


}
