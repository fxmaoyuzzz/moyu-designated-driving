package com.moyu.daijia.common.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author fxmao
 * @date 2024-06-27 14:17
 */
@Configuration
public class DefaultThreadPoolExecutor {


    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {

        //动态获取服务器核数
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor defaultThreadPoolExecutor = new ThreadPoolExecutor(
                processors + 1,
                processors + 1,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        return defaultThreadPoolExecutor;
    }
}
