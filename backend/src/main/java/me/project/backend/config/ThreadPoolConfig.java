package me.project.backend.config;

import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        return new ThreadPoolTaskExecutorBuilder().build();
    }
}
