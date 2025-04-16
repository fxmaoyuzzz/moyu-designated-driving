package com.moyu.daijia.customer.service.impl;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.coupon.client.CouponFeignClient;
import com.moyu.daijia.customer.client.CustomerInfoFeignClient;
import com.moyu.daijia.customer.service.OrderService;
import com.moyu.daijia.dispatch.client.NewOrderFeignClient;
import com.moyu.daijia.driver.client.DriverInfoFeignClient;
import com.moyu.daijia.map.client.LocationFeignClient;
import com.moyu.daijia.map.client.MapFeignClient;
import com.moyu.daijia.map.client.WxPayFeignClient;
import com.moyu.daijia.model.entity.order.OrderInfo;
import com.moyu.daijia.model.entity.payment.PaymentInfo;
import com.moyu.daijia.model.enums.OrderStatus;
import com.moyu.daijia.model.form.coupon.UseCouponForm;
import com.moyu.daijia.model.form.customer.ExpectOrderForm;
import com.moyu.daijia.model.form.customer.SubmitOrderForm;
import com.moyu.daijia.model.form.map.CalculateDrivingLineForm;
import com.moyu.daijia.model.form.order.OrderInfoForm;
import com.moyu.daijia.model.form.payment.CreateWxPaymentForm;
import com.moyu.daijia.model.form.payment.PaymentInfoForm;
import com.moyu.daijia.model.form.rules.FeeRuleRequestForm;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.customer.ExpectOrderVo;
import com.moyu.daijia.model.vo.dispatch.NewOrderTaskVo;
import com.moyu.daijia.model.vo.driver.DriverInfoVo;
import com.moyu.daijia.model.vo.map.DrivingLineVo;
import com.moyu.daijia.model.vo.map.OrderLocationVo;
import com.moyu.daijia.model.vo.map.OrderServiceLastLocationVo;
import com.moyu.daijia.model.vo.order.CurrentOrderInfoVo;
import com.moyu.daijia.model.vo.order.OrderBillVo;
import com.moyu.daijia.model.vo.order.OrderInfoVo;
import com.moyu.daijia.model.vo.order.OrderPayVo;
import com.moyu.daijia.model.vo.payment.WxPrepayVo;
import com.moyu.daijia.model.vo.rules.FeeRuleResponseVo;
import com.moyu.daijia.order.client.OrderInfoFeignClient;
import com.moyu.daijia.rules.client.FeeRuleFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;

    @Autowired
    private LocationFeignClient locationFeignClient;

    @Autowired
    private WxPayFeignClient wxPayFeignClient;

    @Autowired
    private CustomerInfoFeignClient customerInfoFeignClient;

    @Autowired
    private CouponFeignClient couponFeignClient;

    @Override
    public ExpectOrderVo expectOrder(ExpectOrderForm expectOrderForm) {
        if (expectOrderForm.getStartPointLatitude().compareTo(BigDecimal.ZERO) == 0
                || expectOrderForm.getStartPointLongitude().compareTo(BigDecimal.ZERO) == 0
                || expectOrderForm.getEndPointLatitude().compareTo(BigDecimal.ZERO) == 0
                || expectOrderForm.getEndPointLongitude().compareTo(BigDecimal.ZERO) == 0) {
            log.warn("预估订单数据失败，参数错误");
            // throw new MoyuException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        // 获取驾驶线路
        CalculateDrivingLineForm calculateDrivingLineForm = new CalculateDrivingLineForm();
        BeanUtils.copyProperties(expectOrderForm, calculateDrivingLineForm);
        Result<DrivingLineVo> drivingLineVoResult = mapFeignClient.calculateDrivingLine(calculateDrivingLineForm);
        log.info("调用远程service-map服务calculateDrivingLine结果：{}", JSON.toJSONString(drivingLineVoResult));

        DrivingLineVo drivingLineVo = drivingLineVoResult.getData();
        ExpectOrderVo expectOrderVo = new ExpectOrderVo();

        if (drivingLineVo != null) {
            // 获取订单费用
            FeeRuleRequestForm calculateOrderFeeForm = new FeeRuleRequestForm();
            calculateOrderFeeForm.setDistance(drivingLineVo.getDistance());
            calculateOrderFeeForm.setStartTime(new Date());
            calculateOrderFeeForm.setWaitMinute(0);
            Result<FeeRuleResponseVo> feeRuleResponseVoResult = feeRuleFeignClient.calculateOrderFee(calculateOrderFeeForm);
            log.info("调用远程service-rules服务calculateOrderFee结果：{}", JSON.toJSONString(feeRuleResponseVoResult));

            FeeRuleResponseVo feeRuleResponseVo = feeRuleResponseVoResult.getData();

            expectOrderVo.setDrivingLineVo(drivingLineVo);
            expectOrderVo.setFeeRuleResponseVo(feeRuleResponseVo);
        }

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

    @Override
    public CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId) {
        CurrentOrderInfoVo data = orderInfoFeignClient.searchCustomerCurrentOrder(customerId).getData();
        log.info("调用远程service-order服务searchCustomerCurrentOrder结果：{}", JSON.toJSONString(data));

        return data;
    }

    @Override
    public OrderInfoVo getOrderInfo(Long orderId, Long customerId) {
        OrderInfo orderInfo = orderInfoFeignClient.getOrderInfo(orderId).getData();
        log.info("调用远程service-order服务getOrderInfo结果：{}", JSON.toJSONString(orderInfo));

        if (!orderInfo.getCustomerId().equals(customerId)) {
            log.warn("查询订单详情失败，当前订单所属的乘客id不一致");
            throw new MoyuException(ResultCodeEnum.ILLEGAL_REQUEST);
        }

        // 获取司机信息
        DriverInfoVo driverInfoVo = null;
        Long driverId = orderInfo.getDriverId();
        if (driverId != null) {
            driverInfoVo = driverInfoFeignClient.getDriverInfo(driverId).getData();
            log.info("调用远程service-driver服务getDriverInfo结果：{}", JSON.toJSONString(driverInfoVo));
        }

        // 获取账单信息
        OrderBillVo orderBillVo = null;
        if (orderInfo.getStatus() >= OrderStatus.UNPAID.getStatus()) {
            orderBillVo = orderInfoFeignClient.getOrderBillInfo(orderId).getData();
        }

        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setOrderId(orderId);
        BeanUtils.copyProperties(orderInfo, orderInfoVo);
        orderInfoVo.setOrderBillVo(orderBillVo);
        orderInfoVo.setDriverInfoVo(driverInfoVo);
        return orderInfoVo;
    }

    @Override
    public DriverInfoVo getDriverInfo(Long orderId, Long customerId) {
        // 根据订单id获取订单信息
        OrderInfo orderInfo = orderInfoFeignClient.getOrderInfo(orderId).getData();
        if (!orderInfo.getCustomerId().equals(customerId)) {
            log.info("获取司机信息失败，当前订单所属乘客id不一致");
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
        DriverInfoVo data = driverInfoFeignClient.getDriverInfo(orderInfo.getDriverId()).getData();
        log.info("调用远程service-order服务getDriverInfoOrder结果：{}", JSON.toJSONString(data));

        return data;
    }

    @Override
    public OrderLocationVo getCacheOrderLocation(Long orderId) {
        OrderLocationVo data = locationFeignClient.getCacheOrderLocation(orderId).getData();
        log.info("调用远程service-map服务getCacheOrderLocation结果：{}", JSON.toJSONString(data));

        return data;
    }

    @Override
    public DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm) {
        DrivingLineVo data = mapFeignClient.calculateDrivingLine(calculateDrivingLineForm).getData();
        log.info("调用远程service-map服务getCacheOrderLocation结果：{}", JSON.toJSONString(data));

        return data;
    }

    @Override
    public OrderServiceLastLocationVo getOrderServiceLastLocation(Long orderId) {
        OrderServiceLastLocationVo data = locationFeignClient.getOrderServiceLastLocation(orderId).getData();
        log.info("调用远程service-map服务getOrderServiceLastLocation结果：{}", JSON.toJSONString(data));

        return data;
    }

    @Override
    public PageVo findCustomerOrderPage(Long customerId, Long page, Long limit) {
        PageVo data = orderInfoFeignClient.findCustomerOrderPage(customerId, page, limit).getData();

        return data;
    }

    @Override
    public WxPrepayVo createWxPayment(CreateWxPaymentForm createWxPaymentForm) {
        // 获取订单支付信息
        OrderPayVo orderPayVo = orderInfoFeignClient.getOrderPayVo(createWxPaymentForm.getOrderNo(),
                createWxPaymentForm.getCustomerId()).getData();
        log.info("调用远程service-order服务getOrderPayVo结果：{}", JSON.toJSONString(orderPayVo));

        // 判断订单状态
        if (!orderPayVo.getStatus().equals(OrderStatus.UNPAID.getStatus())) {
            log.warn("发起微信支付异常，订单不是待支付状态");
            throw new MoyuException(ResultCodeEnum.ILLEGAL_REQUEST);
        }

        // 获取乘客和司机openid
        String customerOpenId = customerInfoFeignClient.getCustomerOpenId(orderPayVo.getCustomerId()).getData();
        log.info("调用远程service-customer服务getOrderServiceLastLocation结果：{}", customerOpenId);
        String driverOpenId = driverInfoFeignClient.getDriverOpenId(orderPayVo.getDriverId()).getData();
        log.info("调用远程service-driver服务getOrderServiceLastLocation结果：{}", driverOpenId);

        // 处理优惠券
        BigDecimal couponAmount = null;
        // 支付时选择过一次优惠券，如果支付失败或未支付，下次支付时不能再次选择，只能使用第一次选中的优惠券（前端已控制，后端再次校验）
        if (null == orderPayVo.getCouponAmount() && null != createWxPaymentForm.getCustomerCouponId() && createWxPaymentForm.getCustomerCouponId() != 0) {
            UseCouponForm useCouponForm = new UseCouponForm();
            useCouponForm.setOrderId(orderPayVo.getOrderId());
            useCouponForm.setCustomerCouponId(createWxPaymentForm.getCustomerCouponId());
            useCouponForm.setOrderAmount(orderPayVo.getPayAmount());
            useCouponForm.setCustomerId(createWxPaymentForm.getCustomerId());
            couponAmount = couponFeignClient.useCoupon(useCouponForm).getData();
        }

        // 更新账单优惠券金额
        // 计算优惠后的实际支付金额
        BigDecimal payAmount = orderPayVo.getPayAmount();
        if (null != couponAmount) {
            Boolean isUpdate = orderInfoFeignClient.updateCouponAmount(orderPayVo.getOrderId(), couponAmount).getData();
            if (!isUpdate) {
                throw new MoyuException(ResultCodeEnum.DATA_ERROR);
            }
            // 当前实际支付金额 = 支付金额 - 优惠券金额
            payAmount = payAmount.subtract(couponAmount);
        }

        // 发起微信支付
        if (payAmount.compareTo(BigDecimal.ZERO) > 0) {
            PaymentInfoForm paymentInfoForm = new PaymentInfoForm();
            paymentInfoForm.setCustomerOpenId(customerOpenId);
            paymentInfoForm.setDriverOpenId(driverOpenId);
            paymentInfoForm.setOrderNo(orderPayVo.getOrderNo());
            paymentInfoForm.setAmount(payAmount);
            paymentInfoForm.setContent(orderPayVo.getContent());
            paymentInfoForm.setPayWay(1);

            WxPrepayVo wxPrepayVo = wxPayFeignClient.createWxPayment(paymentInfoForm).getData();
        }

        // 保存支付记录，便于测试支付成功
        buildPaymentInfo(orderPayVo, customerOpenId, driverOpenId, payAmount);


        // TODO: 2024/6/27 为了测试 不调接口 支付默认成功
        WxPrepayVo wxPrepayVo = new WxPrepayVo();

        return wxPrepayVo;
    }

    private void buildPaymentInfo(OrderPayVo orderPayVo, String customerOpenId, String driverOpenId, BigDecimal payAmount) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCustomerOpenId(customerOpenId);
        paymentInfo.setDriverOpenId(driverOpenId);
        paymentInfo.setOrderNo(orderPayVo.getOrderNo());
        paymentInfo.setPayWay(1101);

        String transactionId = java.util.UUID.randomUUID().toString();
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setAmount(payAmount);
        paymentInfo.setContent("代驾费");
        paymentInfo.setPaymentStatus(0);
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent("代驾费支付成功，transactionId" + transactionId);

        wxPayFeignClient.savePaymentInfo(paymentInfo);

    }

    @Override
    public Boolean queryPayStatus(String orderNo) {
        return wxPayFeignClient.queryPayStatus(orderNo).getData();
    }
}
