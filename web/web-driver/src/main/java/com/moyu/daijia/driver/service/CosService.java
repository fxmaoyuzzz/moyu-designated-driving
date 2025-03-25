package com.moyu.daijia.driver.service;

import com.moyu.daijia.model.vo.driver.CosUploadVo;
import org.springframework.web.multipart.MultipartFile;

public interface CosService {


    /**
     * 腾讯云文件上传
     * @param file
     * @param path
     * @return
     */
    CosUploadVo uploadFile(MultipartFile file, String path);
}
