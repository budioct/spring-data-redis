package com.tutorial.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    /**
     * create: 1
     * Data Entity yang akan di jadikan Stream Listener dengan konsep Event Listener,, datanya unutk Publisher yang nantinya akan di Subscriber/ Consumer
     */

    private String id;

    private Long amount;

}
