package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.vo.driver.DriverLicenseOcrVo;
import com.moyu.daijia.model.vo.driver.IdCardOcrVo;
import org.springframework.web.multipart.MultipartFile;

public interface OcrService {

    /**
     * 身份证识别
     *
     * @param file
     * @return
     */
    IdCardOcrVo idCardOcr(MultipartFile file);

    /**
     * 驾驶证识别
     *
     * @param file
     * @return
     */
    DriverLicenseOcrVo driverLicenseOcr(MultipartFile file);
}
