package com.moyu.daijia.driver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fxmao
 * @date 2024-06-25 16:45
 */
@Data
@Component
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentCloudProperties {

    private String secretId;
    private String secretKey;
    private String region;
    private String bucketPrivate;

    private String persionGroupId;

}
