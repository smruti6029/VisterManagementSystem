package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
    	
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        
        jedisConnectionFactory.setHostName("localhost"); 
        
        jedisConnectionFactory.setPort(6379); 
        
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
    	
    	 RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    	 
         redisTemplate.setConnectionFactory(redisConnectionFactory());
         
         redisTemplate.setKeySerializer(new StringRedisSerializer());
         
         redisTemplate.setHashKeySerializer(new StringRedisSerializer()); 
         // Added hash key serializer
         redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
         
         redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
         
         redisTemplate.afterPropertiesSet();
        
        return redisTemplate;
    }
}
