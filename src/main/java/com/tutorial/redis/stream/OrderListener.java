package com.tutorial.redis.stream;

import com.tutorial.data.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderListener implements StreamListener<String, ObjectRecord<String, Order>> {

    /**
     * create: 2
     * Stream Listener
     *  kelas ini harapanya saat kita masukan data Order ke dalam StreamListener method onMessage() akan di panggil secara otomatis
     */

    @Override
    public void onMessage(ObjectRecord<String, Order> message) {
        Order order = message.getValue();
        log.info("Receive order: {}", order);
    } // void onMessage(V message) // akan di panggil ketika ada data baru masuk ketika masuk StreamListener<K, V extends Record<K, ?>>

    /**
     * result:
     * 2023-11-07T15:55:44.491+07:00  WARN 14248 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-07T15:55:49.409+07:00  INFO 14248 --- [cTaskExecutor-1] com.tutorial.redis.stream.OrderListener         : Receive order: Order(id=594e91d2-2890-4f22-a221-d283eee0cc06, amount=1000)
     * 2023-11-07T15:55:54.425+07:00  WARN 14248 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-07T15:55:59.416+07:00  INFO 14248 --- [cTaskExecutor-1] com.tutorial.redis.stream.OrderListener         : Receive order: Order(id=7ec235ee-7572-4681-a3a2-fc788d8b23c1, amount=1000)
     * 2023-11-07T15:56:04.428+07:00  WARN 14248 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-07T15:56:09.406+07:00  INFO 14248 --- [cTaskExecutor-1] com.tutorial.redis.stream.OrderListener         : Receive order: Order(id=d7266f11-f903-4bd1-9603-0f7b1b20f8f0, amount=1000)
     * 2023-11-07T15:56:14.433+07:00  WARN 14248 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-07T15:56:19.407+07:00  INFO 14248 --- [cTaskExecutor-1] com.tutorial.redis.stream.OrderListener         : Receive order: Order(id=98da5f40-8e97-459e-885c-94a080dc981a, amount=1000)
     * 2023-11-07T15:56:24.428+07:00  WARN 14248 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-07T15:56:29.405+07:00  INFO 14248 --- [cTaskExecutor-1] com.tutorial.redis.stream.OrderListener         : Receive order: Order(id=ade86123-59e1-43a0-9297-9e3c12ea21e5, amount=1000)
     * 2023-11-07T15:56:34.418+07:00  WARN 14248 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis command timed out
     * 2023-11-07T15:56:36.511+07:00  WARN 14248 --- [cTaskExecutor-1] io.lettuce.core.RedisChannelHandler      : Connection is already closed
     * 2023-11-07T15:56:36.512+07:00  WARN 14248 --- [cTaskExecutor-1] com.tutorial.config.RedisConfig          : Redis exception
     */

}
