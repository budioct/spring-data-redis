package com.tutorial.config;

import com.tutorial.data.Order;
import com.tutorial.redis.message.CustomerListener;
import com.tutorial.redis.stream.OrderListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

@Slf4j
@Configuration
public class RedisConfig {

    @Autowired
    StringRedisTemplate redisTemplate; // extends RedisTemplate<String, String> // object implement Resis Template

    // config stream
    /**
     * create: 4
     * Subscription
     * kita tentukan mau pakai streamListner yang mana, order-group yang mana dan seterusnya
     * caranya kita perlu registrasikan Stream Listener ke Listener Container
     * hasilnya adalah Subscription
     */
    @Bean
    public Subscription ordersSubscription(StreamMessageListenerContainer<String, ObjectRecord<String, Order>> orderContainer,
                                           OrderListener orderListener){

        try {
            redisTemplate.opsForStream().createGroup("orders", "my-group"); // membuat key dan group redistemplate
        } catch (Throwable throwable) {
            // consumer group sudah ada
        }

        StreamOffset<String> offset = StreamOffset.create("orders", ReadOffset.lastConsumed()); // set streamoffset dengan key redistemplate
        Consumer consumer = Consumer.from("my-group", "consumer-1"); // consumer nya dari redistemplate
        var readRequest = StreamMessageListenerContainer.StreamReadRequest
                .builder(offset)
                .consumer(consumer)
                .autoAcknowledge(true) // kalau sudah baca mau di angap sudah selesai baca seacara otomatis. ketika app di jalankan lagi akan baca yang terakhir
                .cancelOnError(throwable -> false) // kalau terjadi error tidak akan di cancle, di lanjutkan terus menerus
                .errorHandler(throwable -> log.warn(throwable.getMessage())) // jika terjadi error maka akan tampilkan pesan
                .build(); // bikin option

        return orderContainer.register(readRequest, orderListener); // Subscription register(StreamReadRequest<K> streamRequest, StreamListener<K, V> listener) // Daftarkan langganan baru untuk Redis Stream.

    }


    /**
     * create: 3
     * Stream Message Listener Container
     * wadah / tempat
     *  untuk menjalankan Stream Listener (OrderListener yang implements StreamListener<K, V extends Record<K, ?>>)
     */
    @Bean(destroyMethod = "stop", initMethod = "start") // destroyMethod ketika app berhenti listener stop, initMethod ketika app running listener start
    public StreamMessageListenerContainer<String, ObjectRecord<String, Order>> orderContainer(RedisConnectionFactory connectionFactory){

        // pollTimeout(Duration) // akan menarik data secara reguler dari StreamListener. note: jika tidak ada data berhenti terus coba lagi, sekali coba sesuai waktu yang di tentukan
        // targetType(Class.class) // type yang telah di set StreamListener
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, Order>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(5))
                .targetType(Order.class)
                .build();

        return StreamMessageListenerContainer.create(connectionFactory, options); // membuat pabrik koneksi redis dengan config dari StreamMessageListenerContainerOptions

    }

    //==================================================================================================================
    //config pubsub

    /**
     * RedisMessageListenerContainer wadah/ tempat untuk menjalankan pubsub listener
     */
    @Bean(destroyMethod = "stop", initMethod = "start") // destroyMethod ketika app berhenti listener stop, initMethod ketika app running listener start
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory connectionFactory, CustomerListener customerListener){

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(customerListener, new ChannelTopic("customers"));
        return container;
    }

}
