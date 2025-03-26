package com.moyu.daijia.dispatch.service;

import com.moyu.daijia.model.vo.dispatch.NewOrderTaskVo;
import com.moyu.daijia.model.vo.order.NewOrderDataVo;

import java.util.List;

public interface NewOrderService {

    /**
     * 添加并开始新订单任务调度
     *
     * @param newOrderTaskVo
     * @return
     */
    Long addAndStartTask(NewOrderTaskVo newOrderTaskVo);

    /**
     * 提交任务
     *
     * @param jobId
     */
    void executeTask(long jobId);

    /**
     * 查询司机新订单数据
     *
     * @param driverId
     * @return
     */
    List<NewOrderDataVo> findNewOrderQueueData(Long driverId);

    /**
     * 清空新订单队列数据
     *
     * @param driverId
     * @return
     */
    Boolean clearNewOrderQueueData(Long driverId);
}
