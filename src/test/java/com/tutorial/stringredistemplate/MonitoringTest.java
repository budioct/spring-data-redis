package com.tutorial.stringredistemplate;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonitoringTest {

    /**
     * Monitoring
     *   Saat kita menggunakan Spring Data Redis di Spring Boot, secara otomatis akan diredistrasikan RedisHealthIndicator
     *   https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/data/redis/RedisHealthIndicator.html
     *   Artinya secara otomatis, saat endpoint health diakses, Spring Boot akan melakukan ping koneksi ke Redis
     */

    /**
     * result redis
     * endpoint: http://localhost:8080/actuator/health
     * ##### ketika redis UP / Running / di jalankan
     * {
     * status: "UP",
     * components: {
     * diskSpace: {
     * status: "UP",
     * details: {
     * total: 498978516992,
     * free: 315248594944,
     * threshold: 10485760,
     * path: "C:\Dev\2023\Java\Spring Boot\Eko Kurniawan Kannedy\belajar-spring-data-redis\.",
     * exists: true
     * }
     * },
     * ping: {
     * status: "UP"
     * },
     * redis: {
     * status: "UP",
     * details: {
     * version: "7.2.3"
     * }
     * }
     * }
     * }
     *
     * ##### ketika redis Down / Berhenti / tidak di jalankan
     * {
     * status: "DOWN",
     * components: {
     * diskSpace: {
     * status: "UP",
     * details: {
     * total: 498978516992,
     * free: 315249303552,
     * threshold: 10485760,
     * path: "C:\Dev\2023\Java\Spring Boot\Eko Kurniawan Kannedy\belajar-spring-data-redis\.",
     * exists: true
     * }
     * },
     * ping: {
     * status: "UP"
     * },
     * redis: {
     * status: "DOWN",
     * details: {
     * error: "org.springframework.dao.QueryTimeoutException: Redis command timed out"
     * }
     * }
     * }
     * }
     */

}
