package com.tutorial.service;

import com.tutorial.data.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductRedisService {

    // @KeySpace("products") // key redis pada entity
    @Cacheable(value = "products", key = "#id") // implement deklarative spring caching.. deklaratif caching akan menyimpan data ketika data nya di ambil, proses selanjutnya ambil data di chaching tidak lagi di koneksi ke DB
    public Product getProduct(String id){

        // block ini akan di jalankan ketika data belum ada di spring caching. ketika ada tidak akan di jalankan
        log.info("Get Product: {}", id);
        return Product.builder()
                .id(id)
                .name("sample")
                .price(100L)
                .build();

    }

    @CachePut(value = "products", key = "#product.id") // mengubah data chache tanpa harus mengakses @Cacheable
    public Product putProduct(Product product){

        log.info("Put Product: {}", product);
        return product;

    }

    @CacheEvict(value = "products", key = "#id") // menghapus data chache
    public void removeProduct(String id){

        log.info("Remove Product: {}", id);

    }

}
