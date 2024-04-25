package com.systemdesign.slidingwindowlog.common.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING, JsonTypeInfo.As.PROPERTY);

        RedisSerializer<Object> redisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisSerializationContext<String, Object> context = RedisSerializationContext
                .<String, Object>newSerializationContext(RedisSerializer.string())
                .value(redisSerializer)
                .hashValue(redisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
