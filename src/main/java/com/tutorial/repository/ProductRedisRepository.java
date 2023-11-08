package com.tutorial.repository;

import com.tutorial.data.Product;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRedisRepository extends KeyValueRepository<Product, String> {

}
