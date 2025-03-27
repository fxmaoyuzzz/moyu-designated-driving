package com.moyu.daijia.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.driver.client.CiFeignClient;
import com.moyu.daijia.driver.service.FileService;
import com.moyu.daijia.driver.service.MonitorService;
import com.moyu.daijia.model.entity.order.OrderMonitor;
import com.moyu.daijia.model.entity.order.OrderMonitorRecord;
import com.moyu.daijia.model.form.order.OrderMonitorForm;
import com.moyu.daijia.model.vo.order.TextAuditingVo;
import com.moyu.daijia.order.client.OrderMonitorFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {


    @Autowired
    private OrderMonitorFeignClient orderMonitorFeignClient;

    @Autowired
    private FileService fileService;

    @Autowired
    private CiFeignClient ciFeignClient;

    @Override
    public Boolean upload(MultipartFile file, OrderMonitorForm orderMonitorForm) {
        // 上传文件
        String url = fileService.upload(file);

        OrderMonitorRecord orderMonitorRecord = new OrderMonitorRecord();
        orderMonitorRecord.setOrderId(orderMonitorForm.getOrderId());
        orderMonitorRecord.setFileUrl(url);
        orderMonitorRecord.setContent(orderMonitorForm.getContent());
        // 增加文本审核
        TextAuditingVo textAuditingVo =
                ciFeignClient.textAuditing(orderMonitorForm.getContent()).getData();
        log.info("调用service-driver服务textAuditing结果：{}", JSON.toJSONString(textAuditingVo));
        orderMonitorRecord.setResult(textAuditingVo.getResult());
        orderMonitorRecord.setKeywords(textAuditingVo.getKeywords());

        orderMonitorFeignClient.saveMonitorRecord(orderMonitorRecord);

        // 更新订单监控统计
        OrderMonitor orderMonitor = orderMonitorFeignClient.getOrderMonitor(orderMonitorForm.getOrderId()).getData();
        int fileNum = orderMonitor.getFileNum() + 1;
        orderMonitor.setFileNum(fileNum);
        // 审核结果: 0（审核正常），1 （判定为违规敏感文件），2（疑似敏感，建议人工复核）。
        if ("3".equals(orderMonitorRecord.getResult())) {
            int auditNum = orderMonitor.getAuditNum() + 1;
            orderMonitor.setAuditNum(auditNum);
        }
        orderMonitorFeignClient.updateOrderMonitor(orderMonitor);

        return true;
    }
}
