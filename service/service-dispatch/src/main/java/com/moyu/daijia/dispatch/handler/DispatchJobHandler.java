package com.moyu.daijia.dispatch.handler;

import com.moyu.daijia.dispatch.mapper.XxlJobLogMapper;
import com.moyu.daijia.dispatch.service.NewOrderService;
import com.moyu.daijia.model.entity.dispatch.XxlJobLog;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author fxmao
 * @date 2024-06-26 15:46
 */
@Component
public class DispatchJobHandler {

    @XxlJob("firstJobHandler")
    public void testJobHandler() {
        System.out.println("xxl-job项目集成测试");
    }

    @Autowired
    private XxlJobLogMapper xxlJobLogMapper;

    @Autowired
    private NewOrderService newOrderService;

    @XxlJob("newOrderTaskHandler")
    public void newOrderTaskHandler() {
        // 记录任务调度日志
        XxlJobLog xxlJobLog = new XxlJobLog();
        xxlJobLog.setJobId(XxlJobHelper.getJobId());
        long startTime = System.currentTimeMillis();

        try {
            // 执行任务：搜索附近代驾司机
            newOrderService.executeTask(XxlJobHelper.getJobId());

            // 成功状态
            xxlJobLog.setStatus(1);
        } catch (Exception e) {
            // 失败状态
            xxlJobLog.setStatus(0);
            xxlJobLog.setError(e.getMessage());
            e.printStackTrace();
        } finally {
            long times = System.currentTimeMillis() - startTime;
            xxlJobLog.setTimes(times);
            xxlJobLogMapper.insert(xxlJobLog);
        }
    }
}
