package com.moyu.daijia.map.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author fxmao
 * @date 2024-06-26 12:56
 */
@Configuration
public class MapConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}