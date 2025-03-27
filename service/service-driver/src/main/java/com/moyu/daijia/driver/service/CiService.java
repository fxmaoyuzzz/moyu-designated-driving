package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.vo.order.TextAuditingVo;

public interface CiService {

    /**
     * 腾讯云数据万象图片审核
     *
     * @param path
     * @return
     */
    Boolean imageAuditing(String path);

    /**
     * 腾讯云数据万象文本审核
     *
     * @param content
     * @return
     */
    TextAuditingVo textAuditing(String content);
}
