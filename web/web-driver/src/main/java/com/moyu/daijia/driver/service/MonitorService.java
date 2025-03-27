package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.form.order.OrderMonitorForm;
import org.springframework.web.multipart.MultipartFile;

public interface MonitorService {

    /**
     * 上传录音
     *
     * @param file
     * @param orderMonitorForm
     * @return
     */
    Boolean upload(MultipartFile file, OrderMonitorForm orderMonitorForm);
}
