package com.moyu.daijia.driver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author fxmao
 * @date 2024-06-27 11:56
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {

    private String endpointUrl;
    private String accessKey;
    private String secreKey;
    private String bucketName;
}
