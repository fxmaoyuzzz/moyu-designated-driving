package com.moyu.daijia.driver.service.impl;

import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.driver.config.TencentCloudProperties;
import com.moyu.daijia.driver.service.CosService;
import com.moyu.daijia.driver.service.OcrService;
import com.moyu.daijia.model.vo.driver.CosUploadVo;
import com.moyu.daijia.model.vo.driver.DriverLicenseOcrVo;
import com.moyu.daijia.model.vo.driver.IdCardOcrVo;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.DriverLicenseOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.DriverLicenseOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class OcrServiceImpl implements OcrService {


    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    @Autowired
    private CosService cosService;

    @Override
    public IdCardOcrVo idCardOcr(MultipartFile file) {
        try {
            // 图片转换base64格式字符串
            byte[] base64 = Base64.encodeBase64(file.getBytes());
            String fileBase64 = new String(base64);

            Credential cred = new Credential(tencentCloudProperties.getSecretId(),
                    tencentCloudProperties.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            OcrClient client = new OcrClient(cred, tencentCloudProperties.getRegion(), clientProfile);
            IDCardOCRRequest req = new IDCardOCRRequest();
            // 设置文件
            req.setImageBase64(fileBase64);

            // 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
            IDCardOCRResponse resp = client.IDCardOCR(req);

            IdCardOcrVo idCardOcrVo = new IdCardOcrVo();
            if (StringUtils.hasText(resp.getName())) {
                // 身份证正面
                idCardOcrVo.setName(resp.getName());
                idCardOcrVo.setGender("男".equals(resp.getSex()) ? "1" : "2");
                idCardOcrVo.setBirthday(DateTimeFormat.forPattern("yyyy/MM/dd").parseDateTime(resp.getBirth()).toDate());
                idCardOcrVo.setIdcardNo(resp.getIdNum());
                idCardOcrVo.setIdcardAddress(resp.getAddress());

                // 上传身份证正面图片到腾讯云cos
                CosUploadVo cosUploadVo = cosService.upload(file, "idCard");
                idCardOcrVo.setIdcardFrontUrl(cosUploadVo.getUrl());
                idCardOcrVo.setIdcardFrontShowUrl(cosUploadVo.getShowUrl());
            } else {
                // 身份证反面
                // 证件有效期："2010.07.21-2020.07.21"
                String idcardExpireString = resp.getValidDate().split("-")[1];
                idCardOcrVo.setIdcardExpire(DateTimeFormat.forPattern("yyyy.MM.dd").parseDateTime(idcardExpireString).toDate());
                // 上传身份证反面图片到腾讯云cos
                CosUploadVo cosUploadVo = cosService.upload(file, "idCard");
                idCardOcrVo.setIdcardBackUrl(cosUploadVo.getUrl());
                idCardOcrVo.setIdcardBackShowUrl(cosUploadVo.getShowUrl());
            }
            return idCardOcrVo;
        } catch (Exception e) {
            log.warn("调用腾讯云OCR识别身份证发生异常：{}", e.getMessage());
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public DriverLicenseOcrVo driverLicenseOcr(MultipartFile file) {
        try{
            //图片转换base64格式字符串
            byte[] base64 = Base64.encodeBase64(file.getBytes());
            String fileBase64 = new String(base64);

            Credential cred = new Credential(tencentCloudProperties.getSecretId(),
                    tencentCloudProperties.getSecretKey());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            OcrClient client = new OcrClient(cred, tencentCloudProperties.getRegion(),
                    clientProfile);
            DriverLicenseOCRRequest req = new DriverLicenseOCRRequest();
            req.setImageBase64(fileBase64);

            DriverLicenseOCRResponse resp = client.DriverLicenseOCR(req);

            DriverLicenseOcrVo driverLicenseOcrVo = new DriverLicenseOcrVo();
            if (StringUtils.hasText(resp.getName())) {
                //驾驶证正面
                //驾驶证名称要与身份证名称一致
                driverLicenseOcrVo.setName(resp.getName());
                driverLicenseOcrVo.setDriverLicenseClazz(resp.getClass_());
                driverLicenseOcrVo.setDriverLicenseNo(resp.getCardCode());
                driverLicenseOcrVo.setDriverLicenseIssueDate(DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(resp.getDateOfFirstIssue()).toDate());
                driverLicenseOcrVo.setDriverLicenseExpire(DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(resp.getEndDate()).toDate());

                //上传驾驶证反面图片到腾讯云cos
                CosUploadVo cosUploadVo = cosService.upload(file, "driverLicense");
                driverLicenseOcrVo.setDriverLicenseFrontUrl(cosUploadVo.getUrl());
                driverLicenseOcrVo.setDriverLicenseFrontShowUrl(cosUploadVo.getShowUrl());
            } else {
                //驾驶证反面
                //上传驾驶证反面图片到腾讯云cos
                CosUploadVo cosUploadVo =  cosService.upload(file, "driverLicense");
                driverLicenseOcrVo.setDriverLicenseBackUrl(cosUploadVo.getUrl());
                driverLicenseOcrVo.setDriverLicenseBackShowUrl(cosUploadVo.getShowUrl());
            }

            return driverLicenseOcrVo;
        } catch (Exception e) {
            log.warn("调用腾讯云OCR识别驾驶证发生异常：{}", e.getMessage());
            // e.printStackTrace();
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
