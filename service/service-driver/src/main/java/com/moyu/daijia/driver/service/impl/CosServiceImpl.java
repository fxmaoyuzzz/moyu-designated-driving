package com.moyu.daijia.driver.service.impl;

import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.driver.config.TencentCloudProperties;
import com.moyu.daijia.driver.service.CiService;
import com.moyu.daijia.driver.service.CosService;
import com.moyu.daijia.model.vo.driver.CosUploadVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class CosServiceImpl implements CosService {


    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    @Autowired
    private CiService ciService;

    @Override
    public CosUploadVo upload(MultipartFile file, String path) {
        // 初始化用户身份信息（secretId, secretKey）。
        COSClient cosClient = this.getCosClient();

        // 文件上传
        // 元数据信息
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(file.getSize());
        meta.setContentEncoding("UTF-8");
        meta.setContentType(file.getContentType());

        // 向存储桶中保存文件
        // 获取文件后缀名
        String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uploadPath = "/driver/" + path + "/" + UUID.randomUUID().toString().replaceAll("-", "") + fileType;

        PutObjectRequest putObjectRequest = null;
        try {
            putObjectRequest = new PutObjectRequest(tencentCloudProperties.getBucketPrivate(),
                    uploadPath,
                    file.getInputStream(),
                    meta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        putObjectRequest.setStorageClass(StorageClass.Standard);
        // 上传文件
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        // 图片审核
        Boolean imageAuditing = ciService.imageAuditing(uploadPath);
        if (!imageAuditing) {
            log.info("图片数据审核不通过，删除违规图片");
            // 删除违规图片
            cosClient.deleteObject(tencentCloudProperties.getBucketPrivate(), uploadPath);
            throw new MoyuException(ResultCodeEnum.IMAGE_AUDITION_FAIL);
        }


        // 返回vo对象
        CosUploadVo cosUploadVo = new CosUploadVo();
        cosUploadVo.setUrl(uploadPath);
        // 图片临时访问url，回显使用
        String imageUrl = this.getImageUrl(uploadPath);
        cosUploadVo.setShowUrl(imageUrl);
        return cosUploadVo;
    }

    @Override
    public String getImageUrl(String path) {
        if (!StringUtils.hasText(path)) {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
        // 获取COSClient对象
        COSClient cosClient = this.getCosClient();
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(tencentCloudProperties.getBucketPrivate(),
                        path, HttpMethodName.GET);
        // 设置临时URL有效期为15分钟
        Date date = new DateTime().plusMinutes(30).toDate();
        request.setExpiration(date);
        // 调用方法获取
        URL url = cosClient.generatePresignedUrl(request);
        log.info("生成腾讯云上传文件临时地址：{}", url);
        cosClient.shutdown();
        return url.toString();
    }

    private COSClient getCosClient() {
        log.info("获取腾讯云文件上传客户端COSClient");
        String secretId = tencentCloudProperties.getSecretId();
        String secretKey = tencentCloudProperties.getSecretKey();
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 设置 bucket 的地域, COS 地域
        Region region = new Region(tencentCloudProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 设置使用 https 协议
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(cred, clientConfig);

        return cosClient;
    }
}
