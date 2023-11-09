package com.tutorial.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@KeySpace("products") // adalah key pada redis.. setiap id akan di pair oleh keys di redis, seperti --> products:1 ~ products:n
public class Product implements Serializable {

    /**
     * karena hasil spring caching object RedisCacheManager adalah byte[] kita perlu implement Serializable agar bisa di simpan bianry data
     */

    @Id // di id pada entity
    private String id;
    private String name;
    private Long price;

    @TimeToLive(unit = TimeUnit.SECONDS) // set waktu expire unutk entity di spring data redis, menentukan berapa lama data harus dihapus di redis
    private Long ttl = -1L; // waktu tidak akan pernah expire

}
