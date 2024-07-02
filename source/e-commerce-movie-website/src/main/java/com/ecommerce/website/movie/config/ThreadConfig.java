package com.ecommerce.website.movie.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Slf4j
public class ThreadConfig implements AsyncConfigurer {
    @Value("${thread.pool.core.size}")
    private Integer corePoolSize;
    @Value("${thread.pool.max.size}")
    private Integer maxPoolSize;
    @Value("${thread.pool.queue.capacity}")
    private Integer queueCapacity;
    @Bean(name = "threadPoolExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("movies-invoke-");
        executor.initialize();
        return executor;
    }
}
