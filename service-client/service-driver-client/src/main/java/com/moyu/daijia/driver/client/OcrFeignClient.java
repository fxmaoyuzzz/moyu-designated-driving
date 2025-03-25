package com.moyu.daijia.driver.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.vo.driver.DriverLicenseOcrVo;
import com.moyu.daijia.model.vo.driver.IdCardOcrVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "service-driver")
public interface OcrFeignClient {


    /**
     * 身份证识别
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/ocr/idCardOcr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<IdCardOcrVo> idCardOcr(@RequestPart("file") MultipartFile file);

    /**
     * 驾驶证识别
     * @param file
     * @return
     */
    @PostMapping(value = "/ocr/driverLicenseOcr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<DriverLicenseOcrVo> driverLicenseOcr(@RequestPart("file") MultipartFile file);
}