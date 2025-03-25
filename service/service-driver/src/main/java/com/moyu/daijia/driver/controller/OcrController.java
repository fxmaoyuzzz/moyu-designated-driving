package com.moyu.daijia.driver.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.driver.service.OcrService;
import com.moyu.daijia.model.vo.driver.DriverLicenseOcrVo;
import com.moyu.daijia.model.vo.driver.IdCardOcrVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "腾讯云识别接口管理")
@RestController
@RequestMapping(value = "/ocr")
public class OcrController {


    @Autowired
    private OcrService ocrService;

    @Operation(summary = "身份证识别")
    @PostMapping("/idCardOcr")
    public Result<IdCardOcrVo> idCardOcr(@RequestPart("file") MultipartFile file) {
        log.info("调用OcrController.idCardOcr接口SUCCESS");
        IdCardOcrVo idCardOcrVo = ocrService.idCardOcr(file);
        return Result.ok(idCardOcrVo);
    }

    @Operation(summary = "驾驶证识别")
    @PostMapping("/driverLicenseOcr")
    public Result<DriverLicenseOcrVo> driverLicenseOcr(@RequestPart("file") MultipartFile file) {
        return Result.ok(ocrService.driverLicenseOcr(file));
    }
}

