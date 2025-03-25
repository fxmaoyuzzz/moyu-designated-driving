package com.moyu.daijia.driver.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.driver.service.CosService;
import com.moyu.daijia.model.vo.driver.CosUploadVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "腾讯云cos上传接口管理")
@RestController
@RequestMapping(value="/cos")
public class CosController {



    @Autowired
    private CosService cosService;

    @Operation(summary = "上传")
    @PostMapping("/upload")
    public Result<CosUploadVo> upload(@RequestPart("file") MultipartFile file,
                                      @RequestParam("path") String path) {
        log.info("调用CosController.upload接口SUCCESS");
        CosUploadVo cosUploadVo = cosService.upload(file,path);
        return Result.ok(cosUploadVo);
    }
}

