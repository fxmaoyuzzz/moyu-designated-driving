package com.moyu.daijia.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.driver.client.CosFeignClient;
import com.moyu.daijia.driver.service.CosService;
import com.moyu.daijia.model.vo.driver.CosUploadVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class CosServiceImpl implements CosService {


    @Autowired
    private CosFeignClient cosFeignClient;

    @Override
    public CosUploadVo uploadFile(MultipartFile file, String path) {
        Result<CosUploadVo> cosUploadVoResult = cosFeignClient.upload(file, path);
        log.info("调用service-driver服务upload接口结果：{}", JSON.toJSONString(cosUploadVoResult));
        CosUploadVo cosUploadVo = cosUploadVoResult.getData();
        return cosUploadVo;
    }
}
