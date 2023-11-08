package com.tutorial.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@KeySpace("products") // adalah key pada redis.. setiap id akan di pair oleh keys di redis, seperti --> products:1 ~ products:n
public class Product {

    @Id // di id pada entity
    private String id;
    private String name;
    private Long price;

}
