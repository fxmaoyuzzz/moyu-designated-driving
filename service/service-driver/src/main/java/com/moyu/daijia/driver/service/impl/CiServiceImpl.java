package com.moyu.daijia.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.driver.config.TencentCloudProperties;
import com.moyu.daijia.driver.service.CiService;
import com.moyu.daijia.model.vo.order.TextAuditingVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ciModel.auditing.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class CiServiceImpl implements CiService {

    private static final String SUCCESS = "Success";

    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    @Override
    public Boolean imageAuditing(String path) {
        ImageAuditingRequest request = new ImageAuditingRequest();
        // 设置请求 bucket
        request.setBucketName(tencentCloudProperties.getBucketPrivate());
        // 设置审核策略 不传则为默认策略（预设）
        // 设置 bucket 中的图片位置
        request.setObjectKey(path);
        COSClient client = this.getCosClient();
        ImageAuditingResponse response = client.imageAuditing(request);
        log.info("调用腾讯云数据万象进行图片审核结果：{}", JSON.toJSONString(response));
        client.shutdown();
        // 用于返回该审核场景的审核结果，返回值：0：正常。1：确认为当前场景的违规内容。2：疑似为当前场景的违规内容。
        if (!response.getPornInfo().getHitFlag().equals("0")
                || !response.getAdsInfo().getHitFlag().equals("0")
                || !response.getTerroristInfo().getHitFlag().equals("0")
                || !response.getPoliticsInfo().getHitFlag().equals("0")
        ) {
            return false;
        }
        return true;
    }

    public COSClient getCosClient() {
        String secretId = tencentCloudProperties.getSecretId();
        String secretKey = tencentCloudProperties.getSecretKey();
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 设置 bucket 的地域, COS 地域
        Region region = new Region(tencentCloudProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    @Override
    public TextAuditingVo textAuditing(String content) {
        if (!StringUtils.hasText(content)) {
            TextAuditingVo textAuditingVo = new TextAuditingVo();
            textAuditingVo.setResult("0");
            return textAuditingVo;
        }

        COSClient cosClient = this.getCosClient();

        TextAuditingRequest request = new TextAuditingRequest();
        request.setBucketName(tencentCloudProperties.getBucketPrivate());
        // 设置请求文本内容的Base64编码
        byte[] encoder = org.apache.commons.codec.binary.Base64.encodeBase64(content.getBytes());
        String contentBase64 = new String(encoder);
        request.getInput().setContent(contentBase64);
        request.getConf().setDetectType("all");

        TextAuditingResponse response = cosClient.createAuditingTextJobs(request);
        log.info("调用腾讯云数据万象进行文本审核结果：{}", JSON.toJSONString(response));
        AuditingJobsDetail detail = response.getJobsDetail();
        TextAuditingVo textAuditingVo = new TextAuditingVo();
        if (SUCCESS.equals(detail.getState())) {
            // 检测结果: 0（审核正常），1 （判定为违规敏感文件），2（疑似敏感，建议人工复核）。
            String result = detail.getResult();

            // 违规关键词
            StringBuffer keywords = new StringBuffer();
            List<SectionInfo> sectionInfoList = detail.getSectionList();
            for (SectionInfo info : sectionInfoList) {

                String pornInfoKeyword = info.getPornInfo().getKeywords();
                String illegalInfoKeyword = info.getIllegalInfo().getKeywords();
                String abuseInfoKeyword = info.getAbuseInfo().getKeywords();

                if (pornInfoKeyword.length() > 0) {
                    keywords.append(pornInfoKeyword).append(",");
                }
                if (illegalInfoKeyword.length() > 0) {
                    keywords.append(illegalInfoKeyword).append(",");
                }
                if (abuseInfoKeyword.length() > 0) {
                    keywords.append(abuseInfoKeyword).append(",");
                }
            }
            textAuditingVo.setResult(result);
            log.info("文本审核违规关键字：{}", keywords.toString());
            textAuditingVo.setKeywords(keywords.toString());
        }
        return textAuditingVo;
    }
}
