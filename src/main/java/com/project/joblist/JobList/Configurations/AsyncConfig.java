package com.project.joblist.JobList.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the core pool size
        executor.setMaxPoolSize(20); // Set the maximum pool size
        executor.setQueueCapacity(100); // Set the queue capacity
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}