package me.project.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class RedisConfig {
    @Bean
    StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisScript<Boolean> likeEntityScript() {
        ClassPathResource classPathResource = new ClassPathResource("likeEntity.lua");
        return RedisScript.of(classPathResource, Boolean.class);
    }

    @Bean
    public RedisScript<Boolean> fetchNewsScript() {
        ClassPathResource classPathResource = new ClassPathResource("fetchNewsInCommunity.lua");
        return RedisScript.of(classPathResource, Boolean.class);
    }
}