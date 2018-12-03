package com.tablefootbal.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConnectorConfiguration {

    @Value("${spring.redis.host}")
    private String redisUrl;

//    @Value("${redis.password}")
//    private String redisPassword;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.database_test}")
    private int redisDb;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisUrl);
        redisConfig.setPort(redisPort);
        redisConfig.setDatabase(redisDb);
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
