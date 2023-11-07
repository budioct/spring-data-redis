package com.tutorial.stringredistemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.domain.geo.Metrics;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        redisTemplate.delete("budhi");

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

        redisTemplate.delete("names");

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

    /**
     * ZSet Operation
     * ● Untuk berinteraksi dengan struktur data Sorted Set di Redis, kita bisa menggunakan ZSetOperations class
     * ● https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/ZSetOperations.html
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

    /**
     * Hash Operation
     *  Untuk berinteraksi dengan struktur data Hash di Redis, kita bisa menggunakan HashOperations class
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/HashOperations.html
     */

    @Test
    void testHashOperation(){

        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash(); // Seperti Map<K,V>

        // menamdabah data satu persatu
//        operations.put("user:1", "id", "1"); // void put(H key, HK hashKey, HV value) // menambah data ke HashOperation<T,V,S>
//        operations.put("user:1", "name", "budhi");
//        operations.put("user:1", "email", "budhi@test.com");

        // menambah data sekaligun dengan collection
        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "budhi");
        map.put("email", "budhi@test.com");

        operations.putAll("user:1", map); // void putAll(H key, Map<? extends HK, ? extends HV> m) // Setel beberapa bidang hash ke beberapa nilai menggunakan data yang disediakan di m. // binding/add Collection dengan/ke HashOperation

        Assertions.assertEquals("1", operations.get("user:1", "id")); // HV get(H key, Object hashKey) // Dapatkan nilai yang diberikan hashKey dari hash di key.
        Assertions.assertEquals("budhi", operations.get("user:1", "name"));
        Assertions.assertEquals("budhi@test.com", operations.get("user:1", "email"));

        redisTemplate.delete("user:1");

    }

    /**
     * Geo Operation
     *  Untuk berinteraksi dengan struktur data Geo di Redis, kita bisa menggunakan GeoOperations class
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/GeoOperations.html
     *
     *  Geospatial
     *   Struktur data geospatial digunakan untuk menyimpan data koordinat
     *   Struktur data ini sangat bagus untuk mencari koordinat terdekat, jarak, radius dan lain-lain
     */

    @Test
    public void testGeoOperation(){

        GeoOperations<String, String> operations = redisTemplate.opsForGeo();

        // titik kordinat dengan object Point(x:longtitude,y:langtitude)
        // longtitude x: 106.822702
        // langtitude y: -6.177590
        operations.add("sellers", new Point(106.822702, -6.177590), "Toko A"); // Long add(K key, Point point, M member) // set key dengan value titik kordinat
        operations.add("sellers", new Point(106.820889, -6.174964), "Toko B");

        Distance distance = operations.distance("sellers", "Toko A", "Toko B", Metrics.KILOMETERS); // Distance distance(K key, M member1, M member2, Metric metric) // Dapatkan Distance/ jarak antara member1 dan member2 yang diberikan Metric.
        Assertions.assertEquals(0.3543, distance.getValue()); // double getValue() // mendapat nilai dari GeoOperations

        GeoResults<RedisGeoCommands.GeoLocation<String>> search = operations.search("sellers", new Circle(
                new Point(106.821825, -6.175105),
                new Distance(5, Metrics.KILOMETERS)
        )); // GeoResults<RedisGeoCommands.GeoLocation<M>> search(K key, Circle within) // Dapatkan anggota dalam batas-batas tertentu Circle. mendapat apa saja dalam radius tersebut dalam km

        Assertions.assertEquals(2, search.getContent().size());
        Assertions.assertEquals("Toko A", search.getContent().get(0).getContent().getName()); // cek jarak radius 5 km dari titik kordinat apakah ada member Toko A
        Assertions.assertEquals("Toko B", search.getContent().get(1).getContent().getName());


        redisTemplate.delete("sellers");
    }


    /**
     * Hyper Log Log Operation
     *  Untuk berinteraksi dengan struktur data Hyper Log Log di Redis, kita bisa menggunakan HyperLogLogOperations class
     *  jadi kalau mau cari jumlah data unique lebih cocok hyperloglog tidak perlu media penyimpanan terlalu besar. tetapi kita tidak bisa mengambil datanya. yang di simpan cuman jumlah data unique nya saja
     *  https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/HyperLogLogOperations.html
     */

    @Test
    void testHyperLogLogOperation(){

        HyperLogLogOperations<String, String> operations = redisTemplate.opsForHyperLogLog(); // Uniq, value yang sama tidak akan di masukan ke collection

        operations.add("traffics", "budhi", "oct", "malik"); // Long add(K key, V... values) // menambahkan data ke hyperLogLog
        operations.add("traffics", "jamal", "oct", "husein");
        operations.add("traffics", "asep", "oct", "rebal");

        Assertions.assertEquals(7L, operations.size("traffics")); // Long size(K... keys) // Mendapatkan jumlah elemen saat ini dalam kunci.

        redisTemplate.delete("traffics");

    }

    /**
     * Transaction
     *  Seperti kita tahu, bahwa Redis mendukung fitur Transaction dengan menggunakan perintah MULTI DAN EXEC
     *  Hal ini juga bisa dilakukan di Spring Data Redis dengan menggunakan RedisTemplate
     *  Namun, agar menggunakan data koneksi ke redis yang sama, maka kita perlu menggunakan perintah RedisTemplate.execute()
     *
     * Transaction
     *  # Seperti pada database relational, redis juga mendukung transaction
     *  # Proses transaction adalah proses dimana kita mengirimkan beberapa perintah, dan perintah tersebut akan dianggap sukses jika semua perintah sukses, jika gagal maka semua perintah harus dibatalkan
     *
     *  # Operasi                   Keterangan
     *  # multi (begin)             Mark the start of a transaction block
     *  # exec (commit)              Execute all commands issued after MULTI
     *  # discard (rollback)        Discard all commands issued after MULTI
     */

    @Test
    void testTransactionRedis(){

        redisTemplate.execute(new SessionCallback<>(){
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi(); // begin
                operations.opsForValue().set("test1", "budhi"); // void set(K key, V value) // set key dan value pada operaion Value
                operations.opsForValue().set("test2", "oct");
                operations.exec(); // comit
                return null;
            }
        }); // <T> T execute(SessionCallback<T> session) //

        Assertions.assertEquals("budhi", redisTemplate.opsForValue().get("test1"));
        Assertions.assertEquals("oct", redisTemplate.opsForValue().get("test2"));

        redisTemplate.delete("test1");
        redisTemplate.delete("test2");

    }

    /**
     * Pipeline
     *  Di kelas Redis, kita pernah belajar tentang pipeline, dimana kita bisa mengirim beberapa perintah secara langsung tanpa harus menunggu balasan satu per satu dari Redis
     *  Hal ini juga bisa dilakukan menggunakan Spring Data Redis menggunakan RedisTemplate.executePipelined()
     *  Return dari executePipelined() akan berisikan List status dari tiap perintah yang kita lakukan
     */

    @Test
    void testPipeline(){

        List<Object> statuses = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                // operasi value akan di tampung di list
                operations.opsForValue().set("test1", "budhi", Duration.ofSeconds(2));
                operations.opsForValue().set("test2", "oct", Duration.ofSeconds(2));
                operations.opsForValue().set("test3", "malik", Duration.ofSeconds(2));
                operations.opsForValue().set("test4", "jamal", Duration.ofSeconds(2));
                return null;
            }
        }); // List<Object> executePipelined(SessionCallback<?> session) // Mengeksekusi objek tindakan tertentu pada koneksi pipa, mengembalikan hasilnya.

        assertThat(statuses, hasSize(4));
        assertThat(statuses, hasItem(true));
        assertThat(statuses, not(hasItem(false)));

    }

    /**
     * Stream Operation (secara manual)
     * ● Untuk berinteraksi dengan struktur data Stream di Redis, kita bisa menggunakan StreamOperations class
     * ● https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/StreamOperations.html
     */

    @Test
    void testPublisher(){

        // data yang di set menjadi publisher

        StreamOperations<String, Object, Object> operations = redisTemplate.opsForStream(); // instance. kontrak interface StreamOperations<K, HK, HV>
        MapRecord<String, String, String> record = MapRecord.create("stream-1", Map.of(
                "name", "budhi",
                "address", "Tangerang"
        )); // <S, K, V> MapRecord<S, K, V> create(S stream, Map<K, V> map) // membuat record

        for (int i = 0; i < 10; i++) {
            operations.add(record); // RecordId add(MapRecord<K, ? extends HK, ? extends HV> record)
        }

    }

    @Test
    void testSubscribe(){

        /**
         * dokumentasi ReadOffset spring data redis
         * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/connection/stream/ReadOffset.html
         */

        // Subscribe/ Consumer  menerima data set dari publisher

        StreamOperations<String, Object, Object> operations = redisTemplate.opsForStream(); // instance. kontrak interface StreamOperations<K, HK, HV>

        try {
            operations.createGroup("stream-1", "sample-group"); // String createGroup(K key, String group) // Buat grup-konsumen di latest offset.
        } catch (RedisSystemException e) {
            // group sudah ada
        }

        // List<MapRecord<K, HK, HV>> read(Consumer consumer, StreamOffset<K>... streams) //  Membaca catatan dari satu atau lebih StreamOffset menggunakan grup-konsumen.
        // static Consumer from(String group, String name) // Ciptakan konsumen baru.
        // static <K> StreamOffset<K> create(K stream, ReadOffset readOffset) // Buat Stream Offset yang diberikan key dan ReadOffset.
        List<MapRecord<String, Object, Object>> records = operations.read(Consumer.from("sample-group", "sample-1"),
                StreamOffset.create("stream-1", ReadOffset.lastConsumed()));

        for (MapRecord<String, Object, Object> record : records) {
            System.out.println(record);
        } // melihat hasil sub

        /**
         * result:
         * MapBackedRecord{recordId=1699341541063-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541068-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541072-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541076-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541080-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541083-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541087-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541091-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541095-0, kvMap={name=budhi, address=Tangerang}}
         * MapBackedRecord{recordId=1699341541098-0, kvMap={name=budhi, address=Tangerang}}
         */

    }




}
