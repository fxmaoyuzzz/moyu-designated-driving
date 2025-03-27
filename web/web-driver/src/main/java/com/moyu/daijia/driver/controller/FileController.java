package com.moyu.daijia.driver.controller;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.driver.service.CosService;
import com.moyu.daijia.driver.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "上传管理接口")
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private CosService cosService;

    @Autowired
    private FileService fileService;

    @Operation(summary = "上传")
    // @GlobalLogin
    @PostMapping("/cos/upload")
    public Result<String> upload(@RequestPart("file") MultipartFile file,
                                 @RequestParam(name = "path", defaultValue = "auth") String path) {
        String url = cosService.uploadFile(file, path);
        return Result.ok(url);
    }

    @Operation(summary = "上传文件到MinIO")
    // @GlobalLogin
    @PostMapping("/upload")
    public Result<String> upload(@RequestPart("file") MultipartFile file) {
        String url = fileService.upload(file);
        return Result.ok(url);
    }
}
