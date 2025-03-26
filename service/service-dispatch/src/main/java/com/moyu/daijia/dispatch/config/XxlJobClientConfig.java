package com.moyu.daijia.dispatch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fxmao
 * @date 2024-06-26 16:24
 */
@Data
@Component
@ConfigurationProperties(prefix = "xxl.job.client")
public class XxlJobClientConfig {

    private Integer jobGroupId;
    private String addUrl;
    private String removeUrl;
    private String startJobUrl;
    private String stopJobUrl;
    private String addAndStartUrl;
}
