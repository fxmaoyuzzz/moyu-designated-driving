package com.moyu.daijia.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.driver.client.OcrFeignClient;
import com.moyu.daijia.driver.service.OcrService;
import com.moyu.daijia.model.vo.driver.DriverLicenseOcrVo;
import com.moyu.daijia.model.vo.driver.IdCardOcrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class OcrServiceImpl implements OcrService {


    @Autowired
    private OcrFeignClient ocrFeignClient;

    @Override
    public IdCardOcrVo idCardOcr(MultipartFile file) {
        Result<IdCardOcrVo> ocrVoResult = ocrFeignClient.idCardOcr(file);
        log.info("调用service-driver服务idCardOcr接口结果：{}", JSON.toJSONString(ocrVoResult));
        IdCardOcrVo idCardOcrVo = ocrVoResult.getData();
        return idCardOcrVo;
    }

    @Override
    public DriverLicenseOcrVo driverLicenseOcr(MultipartFile file) {
        Result<DriverLicenseOcrVo> driverLicenseOcrVoResult = ocrFeignClient.driverLicenseOcr(file);
        log.info("调用service-driver服务driverLicenseOcr接口结果：{}", JSON.toJSONString(driverLicenseOcrVoResult));

        DriverLicenseOcrVo driverLicenseOcrVo = driverLicenseOcrVoResult.getData();
        return driverLicenseOcrVo;
    }
}
