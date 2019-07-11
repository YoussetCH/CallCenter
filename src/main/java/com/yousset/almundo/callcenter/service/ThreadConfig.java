/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yousset.almundo.callcenter.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author Yousset
 */
@Configuration
public class ThreadConfig {

    public static final int MAX_NUMBER_SIM_CALLS = 10;

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(MAX_NUMBER_SIM_CALLS);
        executor.setMaxPoolSize(MAX_NUMBER_SIM_CALLS * 2);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        
        return executor;
    }
}
