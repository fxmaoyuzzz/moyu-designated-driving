package com.moyu.daijia.driver.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.constant.SystemConstant;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.common.util.LocationUtil;
import com.moyu.daijia.dispatch.client.NewOrderFeignClient;
import com.moyu.daijia.driver.service.OrderService;
import com.moyu.daijia.map.client.LocationFeignClient;
import com.moyu.daijia.map.client.MapFeignClient;
import com.moyu.daijia.model.entity.order.OrderInfo;
import com.moyu.daijia.model.enums.OrderStatus;
import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.form.order.OrderFeeForm;
import com.moyu.daijia.model.form.order.StartDriveForm;
import com.moyu.daijia.model.form.order.UpdateOrderBillForm;
import com.moyu.daijia.model.form.order.UpdateOrderCartForm;
import com.moyu.daijia.model.form.rules.FeeRuleRequestForm;
import com.moyu.daijia.model.form.rules.ProfitsharingRuleRequestForm;
import com.moyu.daijia.model.form.rules.RewardRuleRequestForm;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.map.DrivingLineVo;
import com.moyu.daijia.model.vo.map.OrderLocationVo;
import com.moyu.daijia.model.vo.map.OrderServiceLastLocationVo;
import com.moyu.daijia.model.vo.order.*;
import com.moyu.daijia.model.vo.rules.FeeRuleResponseVo;
import com.moyu.daijia.model.vo.rules.ProfitsharingRuleResponseVo;
import com.moyu.daijia.model.vo.rules.RewardRuleResponseVo;
import com.moyu.daijia.order.client.OrderInfoFeignClient;
import com.moyu.daijia.rules.client.FeeRuleFeignClient;
import com.moyu.daijia.rules.client.ProfitsharingRuleFeignClient;
import com.moyu.daijia.rules.client.RewardRuleFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private NewOrderFeignClient newOrderFeignClient;

    @Autowired
    private MapFeignClient mapFeignClient;

    @Autowired
    private LocationFeignClient locationFeignClient;

    @Autowired
    private FeeRuleFeignClient feeRuleFeignClient;

    @Autowired
    private RewardRuleFeignClient rewardRuleFeignClient;

    @Autowired
    private ProfitsharingRuleFeignClient profitsharingRuleFeignClient;


    @Override
    public Integer getOrderStatus(Long orderId) {
        Integer result = orderInfoFeignClient.getOrderStatus(orderId).getData();
        log.info("调用远程service-order服务getOrderStatus结果：{}", result);

        return result;
    }

    @Override
    public List<NewOrderDataVo> findNewOrderQueueData(Long driverId) {
        List<NewOrderDataVo> list = newOrderFeignClient.findNewOrderQueueData(driverId).getData();
        log.info("调用远程service-dispatch服务findNewOrderQueueData结果数量：{}", list.size());

        return list;
    }

    @Override
    public Boolean robNewOrder(Long driverId, Long orderId) {
        Boolean data = orderInfoFeignClient.robNewOrder(driverId, orderId).getData();
        log.info("调用远程service-order服务robNewOrder结果：{}", data);

        return data;
    }

    @Override
    public CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId) {
        CurrentOrderInfoVo data = orderInfoFeignClient.searchDriverCurrentOrder(driverId).getData();
        log.info("调用远程service-order服务searchDriverCurrentOrder结果：{}", JSON.toJSONString(data));

        return data;
    }

    @Override
    public OrderInfoVo getOrderInfo(Long orderId, Long driverId) {
        OrderInfo orderInfo = orderInfoFeignClient.getOrderInfo(orderId).getData();
        log.info("调用远程service-order服务getOrderInfo结果：{}", JSON.toJSONString(orderInfo));

        if (!orderInfo.getDriverId().equals(driverId)) {
            log.warn("查询司机账单详情失败，订单所属司机id不一致");
            throw new MoyuException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        // 获取账单和分账数据
        OrderBillVo orderBillVo = null;
        OrderProfitsharingVo orderProfitsharingVo = null;
        if (orderInfo.getStatus() >= OrderStatus.END_SERVICE.getStatus()) {
            // 账单信息
            orderBillVo = orderInfoFeignClient.getOrderBillInfo(orderId).getData();
            log.info("调用远程service-order服务getOrderBillInfo结果：{}", JSON.toJSONString(orderBillVo));


            // 分账信息
            orderProfitsharingVo = orderInfoFeignClient.getOrderProfitsharing(orderId).getData();
            log.info("调用远程service-order服务getOrderProfitsharing结果：{}", JSON.toJSONString(orderProfitsharingVo));
        }

        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setOrderId(orderId);
        BeanUtils.copyProperties(orderInfo, orderInfoVo);
        orderInfoVo.setOrderBillVo(orderBillVo);
        orderInfoVo.setOrderProfitsharingVo(orderProfitsharingVo);
        return orderInfoVo;
    }

    @Override
    public DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm) {
        DrivingLineVo data = mapFeignClient.calculateDrivingLine(calculateDrivingLineForm).getData();
        log.info("调用远程service-map服务calculateDrivingLine结果：{}", JSON.toJSONString(data));

        return data;
    }

    @Override
    public Boolean driverArriveStartLocation(Long orderId, Long driverId) {
        // 判断
        // orderInfo有代驾开始位置
        OrderInfo orderInfo = orderInfoFeignClient.getOrderInfo(orderId).getData();
        log.info("调用远程service-order服务getOrderInfo结果：{}", JSON.toJSONString(orderInfo));

        // 司机当前位置
        OrderLocationVo orderLocationVo = locationFeignClient.getCacheOrderLocation(orderId).getData();
        log.info("调用远程service-map服务getCacheOrderLocation结果：{}", JSON.toJSONString(orderLocationVo));

        // 计算司机当前位置和代驾开始位置距离
        double distance = LocationUtil.getDistance(orderInfo.getStartPointLatitude().doubleValue(),
                orderInfo.getStartPointLongitude().doubleValue(),
                orderLocationVo.getLatitude().doubleValue(),
                orderLocationVo.getLongitude().doubleValue());
        // 司机的实际位置与代驾起始点位置的距离大于1km 判定违规
        if (distance > SystemConstant.DRIVER_START_LOCATION_DISTION) {
            log.warn("当前司机实际位置和接单开始位置大于1km，判定违规");
            throw new MoyuException(ResultCodeEnum.DRIVER_START_LOCATION_DISTION_ERROR);
        }
        Boolean data = orderInfoFeignClient.driverArriveStartLocation(orderId, driverId).getData();
        log.info("调用远程service-order服务driverArriveStartLocation结果：{}", data);

        return data;
    }

    @Override
    public Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm) {
        Boolean data = orderInfoFeignClient.updateOrderCart(updateOrderCartForm).getData();
        log.info("调用远程service-order服务updateOrderCart结果：{}", data);

        return data;
    }

    @Override
    public Boolean startDrive(StartDriveForm startDriveForm) {
        Boolean data = orderInfoFeignClient.startDrive(startDriveForm).getData();
        log.info("调用远程service-order服务startDrive结果：{}", data);

        return data;
    }

    @Override
    public Boolean endDrive(OrderFeeForm orderFeeForm) throws ExecutionException, InterruptedException {
        log.info("================开始结束代驾订单并计算费用======================");
        long start = System.currentTimeMillis();
        // 根据orderId获取订单信息，判断当前订单是否司机接单
        CompletableFuture<OrderInfo> orderInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            OrderInfo orderInfo = orderInfoFeignClient.getOrderInfo(orderFeeForm.getOrderId()).getData();
            if (!orderInfo.getDriverId().equals(orderFeeForm.getDriverId())) {
                log.warn("结束代驾失败，当前订单所属司机不一致");
                throw new MoyuException(ResultCodeEnum.ILLEGAL_REQUEST);
            }
            return orderInfo;
        });

        // 防止刷单
        CompletableFuture<OrderServiceLastLocationVo> orderServiceLastLocationVoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            OrderServiceLastLocationVo orderServiceLastLocationVo = locationFeignClient.getOrderServiceLastLocation(orderFeeForm.getOrderId()).getData();
            return orderServiceLastLocationVo;
        });

        // 合并上面两个future结果
        CompletableFuture.allOf(orderInfoCompletableFuture, orderServiceLastLocationVoCompletableFuture).join();
        // 获取两个线程执行结果
        OrderInfo orderInfo = orderInfoCompletableFuture.get();
        OrderServiceLastLocationVo orderServiceLastLocationVo = orderServiceLastLocationVoCompletableFuture.get();

        // 计算司机当前位置距离结束代驾位置
        double distance = LocationUtil.getDistance(orderInfo.getEndPointLatitude().doubleValue(),
                orderInfo.getEndPointLongitude().doubleValue(),
                orderServiceLastLocationVo.getLatitude().doubleValue(),
                orderServiceLastLocationVo.getLongitude().doubleValue());

        // if (distance > SystemConstant.DRIVER_END_LOCATION_DISTION) {
        //     log.warn("当前司机实际位置和结束代驾位置大于1km，判断违规");
        //     throw new MoyuException(ResultCodeEnum.DRIVER_END_LOCATION_DISTION_ERROR);
        // }

        // 计算订单实际里程
        CompletableFuture<BigDecimal> realDistanceCompletableFuture = CompletableFuture.supplyAsync(() -> {
            BigDecimal realDistance =
                    locationFeignClient.calculateOrderRealDistance(orderFeeForm.getOrderId()).getData();
            log.info("调用远程service-map服务calculateOrderRealDistance结果：{}", realDistance);

            return realDistance;
        });

        CompletableFuture<FeeRuleResponseVo> feeRuleResponseVoCompletableFuture =
                realDistanceCompletableFuture.thenApplyAsync((realDistance) -> {
                    // 计算代驾费用
                    FeeRuleRequestForm feeRuleRequestForm = new FeeRuleRequestForm();
                    feeRuleRequestForm.setDistance(realDistance);
                    feeRuleRequestForm.setStartTime(orderInfo.getStartServiceTime());

                    // 计算司机到达代驾开始位置时间（分钟）
                    Integer waitMinute = Math.abs((int) ((orderInfo.getArriveTime().getTime() - orderInfo.getAcceptTime().getTime()) / (1000 * 60)));
                    feeRuleRequestForm.setWaitMinute(waitMinute);
                    // 代驾费用
                    FeeRuleResponseVo feeRuleResponseVo = feeRuleFeignClient.calculateOrderFee(feeRuleRequestForm).getData();
                    // 实际费用 = 代驾费用 + 其他费用（停车费）
                    BigDecimal totalAmount =
                            feeRuleResponseVo.getTotalAmount().add(orderFeeForm.getTollFee())
                                    .add(orderFeeForm.getParkingFee())
                                    .add(orderFeeForm.getOtherFee())
                                    .add(orderInfo.getFavourFee());
                    feeRuleResponseVo.setTotalAmount(totalAmount);
                    return feeRuleResponseVo;
                });

        // 计算系统奖励
        CompletableFuture<Long> orderNumCompletableFuture = CompletableFuture.supplyAsync(() -> {
            String startTime = new DateTime(orderInfo.getStartServiceTime()).toString("yyyy-MM-dd") + " 00:00:00";
            String endTime = new DateTime(orderInfo.getStartServiceTime()).toString("yyyy-MM-dd") + " 24:00:00";
            Long orderNum = orderInfoFeignClient.getOrderNumByTime(startTime, endTime).getData();
            return orderNum;
        });

        // 计算订单奖励费用
        CompletableFuture<RewardRuleResponseVo> rewardRuleResponseVoCompletableFuture =
                orderNumCompletableFuture.thenApplyAsync((orderNum) -> {
                    RewardRuleRequestForm rewardRuleRequestForm = new RewardRuleRequestForm();
                    rewardRuleRequestForm.setStartTime(orderInfo.getStartServiceTime());
                    rewardRuleRequestForm.setOrderNum(orderNum);
                    RewardRuleResponseVo rewardRuleResponseVo = rewardRuleFeignClient.calculateOrderRewardFee(rewardRuleRequestForm).getData();
                    log.info("调用远程service-rules服务calculateOrderRewardFee结果：{}", JSON.toJSONString(rewardRuleResponseVo));

                    return rewardRuleResponseVo;
                });

        // 计算分账信息
        CompletableFuture<ProfitsharingRuleResponseVo> profitsharingRuleResponseVoCompletableFuture = feeRuleResponseVoCompletableFuture.thenCombineAsync(orderNumCompletableFuture,
                (feeRuleResponseVo, orderNum) -> {
                    ProfitsharingRuleRequestForm profitsharingRuleRequestForm = new ProfitsharingRuleRequestForm();
                    profitsharingRuleRequestForm.setOrderAmount(feeRuleResponseVo.getTotalAmount());
                    profitsharingRuleRequestForm.setOrderNum(orderNum);

                    ProfitsharingRuleResponseVo profitsharingRuleResponseVo = profitsharingRuleFeignClient.calculateOrderProfitsharingFee(profitsharingRuleRequestForm).getData();
                    log.info("调用远程service-rules服务calculateOrderProfitsharingFee结果：{}", JSON.toJSONString(profitsharingRuleResponseVo));
                    return profitsharingRuleResponseVo;
                });

        // 合并
        CompletableFuture.allOf(
                orderInfoCompletableFuture,
                realDistanceCompletableFuture,
                feeRuleResponseVoCompletableFuture,
                orderNumCompletableFuture,
                rewardRuleResponseVoCompletableFuture,
                profitsharingRuleResponseVoCompletableFuture
        ).join();

        // 获取执行结果
        BigDecimal realDistance = realDistanceCompletableFuture.get();
        FeeRuleResponseVo feeRuleResponseVo = feeRuleResponseVoCompletableFuture.get();
        RewardRuleResponseVo rewardRuleResponseVo = rewardRuleResponseVoCompletableFuture.get();
        ProfitsharingRuleResponseVo profitsharingRuleResponseVo = profitsharingRuleResponseVoCompletableFuture.get();

        // 结束代驾更新订单 添加账单和分账信息
        UpdateOrderBillForm updateOrderBillForm = new UpdateOrderBillForm();
        updateOrderBillForm.setOrderId(orderFeeForm.getOrderId());
        updateOrderBillForm.setDriverId(orderFeeForm.getDriverId());
        // 路桥费、停车费、其他费用
        updateOrderBillForm.setTollFee(orderFeeForm.getTollFee());
        updateOrderBillForm.setParkingFee(orderFeeForm.getParkingFee());
        updateOrderBillForm.setOtherFee(orderFeeForm.getOtherFee());
        // 乘客打赏
        updateOrderBillForm.setFavourFee(orderInfo.getFavourFee());
        // 实际里程
        updateOrderBillForm.setRealDistance(realDistance);
        // 订单奖励信息
        BeanUtils.copyProperties(rewardRuleResponseVo, updateOrderBillForm);
        // 代驾费用信息
        BeanUtils.copyProperties(feeRuleResponseVo, updateOrderBillForm);
        // 分账相关信息
        BeanUtils.copyProperties(profitsharingRuleResponseVo, updateOrderBillForm);
        updateOrderBillForm.setProfitsharingRuleId(profitsharingRuleResponseVo.getProfitsharingRuleId());
        orderInfoFeignClient.endDrive(updateOrderBillForm);

        long end = System.currentTimeMillis();
        log.info("================完成结束代驾订单及计算费用======================耗时：{}", end - start);

        return true;
    }

    @Override
    public PageVo findDriverOrderPage(Long driverId, Long page, Long limit) {
        return orderInfoFeignClient.findDriverOrderPage(driverId, page, limit).getData();
    }

    @Override
    public Boolean sendOrderBillInfo(Long orderId, Long driverId) {
        return orderInfoFeignClient.sendOrderBillInfo(orderId, driverId).getData();
    }
}
