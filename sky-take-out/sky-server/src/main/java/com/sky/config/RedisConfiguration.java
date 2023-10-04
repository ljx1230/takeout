package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("RedisTemplate 创建....");
        RedisTemplate redisTemplate = new RedisTemplate<>();
        // 设置序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
