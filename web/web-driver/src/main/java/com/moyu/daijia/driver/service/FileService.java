package com.moyu.daijia.driver.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传文件到MinIO
     *
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
