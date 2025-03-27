package com.moyu.daijia.driver.service;

import org.springframework.web.multipart.MultipartFile;

public interface CosService {


    /**
     * 腾讯云文件上传
     * @param file
     * @param path
     * @return
     */
    String uploadFile(MultipartFile file, String path);
}
