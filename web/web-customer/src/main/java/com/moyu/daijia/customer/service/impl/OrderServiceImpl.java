package com.moyu.daijia.customer.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.customer.service.OrderService;
import com.moyu.daijia.dispatch.client.NewOrderFeignClient;
import com.moyu.daijia.map.client.MapFeignClient;
import com.moyu.daijia.model.form.customer.ExpectOrderForm;
import com.moyu.daijia.model.form.customer.SubmitOrderForm;
import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.form.order.OrderInfoForm;
import com.moyu.daijia.model.form.rules.FeeRuleRequestForm;
import com.moyu.daijia.model.vo.customer.ExpectOrderVo;
import com.moyu.daijia.model.vo.dispatch.NewOrderTaskVo;
import com.moyu.daijia.model.vo.map.DrivingLineVo;
import com.moyu.daijia.model.vo.rules.FeeRuleResponseVo;
import com.moyu.daijia.order.client.OrderInfoFeignClient;
import com.moyu.daijia.rules.client.FeeRuleFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MapFeignClient mapFeignClient;

    @Autowired
    private FeeRuleFeignClient feeRuleFeignClient;

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private NewOrderFeignClient newOrderFeignClient;

    @Override
    public ExpectOrderVo expectOrder(ExpectOrderForm expectOrderForm) {
        // 获取驾驶线路
        CalculateDrivingLineForm calculateDrivingLineForm = new CalculateDrivingLineForm();
        BeanUtils.copyProperties(expectOrderForm, calculateDrivingLineForm);
        Result<DrivingLineVo> drivingLineVoResult = mapFeignClient.calculateDrivingLine(calculateDrivingLineForm);
        log.info("调用远程service-map服务calculateDrivingLine结果：{}", JSON.toJSONString(drivingLineVoResult));

        DrivingLineVo drivingLineVo = drivingLineVoResult.getData();

        // 获取订单费用
        FeeRuleRequestForm calculateOrderFeeForm = new FeeRuleRequestForm();
        calculateOrderFeeForm.setDistance(drivingLineVo.getDistance());
        calculateOrderFeeForm.setStartTime(new Date());
        calculateOrderFeeForm.setWaitMinute(0);
        Result<FeeRuleResponseVo> feeRuleResponseVoResult = feeRuleFeignClient.calculateOrderFee(calculateOrderFeeForm);
        log.info("调用远程service-rules服务calculateOrderFee结果：{}", JSON.toJSONString(feeRuleResponseVoResult));

        FeeRuleResponseVo feeRuleResponseVo = feeRuleResponseVoResult.getData();

        ExpectOrderVo expectOrderVo = new ExpectOrderVo();
        expectOrderVo.setDrivingLineVo(drivingLineVo);
        expectOrderVo.setFeeRuleResponseVo(feeRuleResponseVo);

        return expectOrderVo;
    }

    @Override
    public Long submitOrder(SubmitOrderForm submitOrderForm) {
        // 重新计算驾驶线路
        CalculateDrivingLineForm calculateDrivingLineForm = new CalculateDrivingLineForm();
        BeanUtils.copyProperties(submitOrderForm, calculateDrivingLineForm);
        Result<DrivingLineVo> drivingLineVoResult = mapFeignClient.calculateDrivingLine(calculateDrivingLineForm);
        log.info("调用远程service-map服务calculateDrivingLine结果：{}", JSON.toJSONString(drivingLineVoResult));

        DrivingLineVo drivingLineVo = drivingLineVoResult.getData();

        // 重新订单费用
        FeeRuleRequestForm calculateOrderFeeForm = new FeeRuleRequestForm();
        calculateOrderFeeForm.setDistance(drivingLineVo.getDistance());
        calculateOrderFeeForm.setStartTime(new Date());
        calculateOrderFeeForm.setWaitMinute(0);
        Result<FeeRuleResponseVo> feeRuleResponseVoResult = feeRuleFeignClient.calculateOrderFee(calculateOrderFeeForm);
        log.info("调用远程service-rules服务calculateOrderFee结果：{}", JSON.toJSONString(feeRuleResponseVoResult));

        FeeRuleResponseVo feeRuleResponseVo = feeRuleResponseVoResult.getData();

        OrderInfoForm orderInfoForm = new OrderInfoForm();
        BeanUtils.copyProperties(submitOrderForm, orderInfoForm);
        orderInfoForm.setExpectDistance(drivingLineVo.getDistance());
        orderInfoForm.setExpectAmount(feeRuleResponseVo.getTotalAmount());
        Result<Long> orderInfoResult = orderInfoFeignClient.saveOrderInfo(orderInfoForm);
        log.info("调用远程service-order服务saveOrderInfo结果：{}", JSON.toJSONString(orderInfoResult));

        Long orderId = orderInfoResult.getData();

        // 查询附近可以接单司机
        NewOrderTaskVo newOrderDispatchVo = new NewOrderTaskVo();
        newOrderDispatchVo.setOrderId(orderId);
        newOrderDispatchVo.setStartLocation(orderInfoForm.getStartLocation());
        newOrderDispatchVo.setStartPointLongitude(orderInfoForm.getStartPointLongitude());
        newOrderDispatchVo.setStartPointLatitude(orderInfoForm.getStartPointLatitude());
        newOrderDispatchVo.setEndLocation(orderInfoForm.getEndLocation());
        newOrderDispatchVo.setEndPointLongitude(orderInfoForm.getEndPointLongitude());
        newOrderDispatchVo.setEndPointLatitude(orderInfoForm.getEndPointLatitude());
        newOrderDispatchVo.setExpectAmount(orderInfoForm.getExpectAmount());
        newOrderDispatchVo.setExpectDistance(orderInfoForm.getExpectDistance());
        newOrderDispatchVo.setExpectTime(drivingLineVo.getDuration());
        newOrderDispatchVo.setFavourFee(orderInfoForm.getFavourFee());
        newOrderDispatchVo.setCreateTime(new Date());

        Long jobId = newOrderFeignClient.addAndStartTask(newOrderDispatchVo).getData();
        log.info("调用远程service-dispatch服务addAndStartTask结果：{}", JSON.toJSONString(jobId));

        return orderId;
    }

    @Override
    public Integer getOrderStatus(Long orderId) {
        Result<Integer> integerResult = orderInfoFeignClient.getOrderStatus(orderId);
        log.info("调用远程service-order服务getOrderStatus结果：{}", JSON.toJSONString(integerResult));

        return integerResult.getData();
    }
}
