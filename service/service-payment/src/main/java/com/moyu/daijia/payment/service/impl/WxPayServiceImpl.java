package com.moyu.daijia.payment.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moyu.daijia.common.constant.MqConst;
import com.moyu.daijia.common.constant.SystemConstant;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.common.service.RabbitService;
import com.moyu.daijia.driver.client.DriverAccountFeignClient;
import com.moyu.daijia.model.entity.order.OrderInfo;
import com.moyu.daijia.model.entity.payment.PaymentInfo;
import com.moyu.daijia.model.enums.TradeType;
import com.moyu.daijia.model.form.driver.TransferForm;
import com.moyu.daijia.model.form.payment.PaymentInfoForm;
import com.moyu.daijia.model.form.payment.ProfitsharingForm;
import com.moyu.daijia.model.vo.order.OrderProfitsharingVo;
import com.moyu.daijia.model.vo.order.OrderRewardVo;
import com.moyu.daijia.model.vo.payment.WxPrepayVo;
import com.moyu.daijia.order.client.OrderInfoFeignClient;
import com.moyu.daijia.payment.config.WxPayV3Properties;
import com.moyu.daijia.payment.mapper.PaymentInfoMapper;
import com.moyu.daijia.payment.service.WxPayService;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private WxPayV3Properties wxPayV3Properties;

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private DriverAccountFeignClient driverAccountFeignClient;

    // @Autowired
    // private RSAAutoCertificateConfig rsaAutoCertificateConfig;

    @Autowired
    private RabbitService rabbitService;


    @Override
    public WxPrepayVo createWxPayment(PaymentInfoForm paymentInfoForm) {
        try {
            // // 添加支付记录到支付表里面
            // // 判断：如果表存在订单支付记录，不需要添加
            // LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
            // wrapper.eq(PaymentInfo::getOrderNo, paymentInfoForm.getOrderNo());
            // PaymentInfo paymentInfo = paymentInfoMapper.selectOne(wrapper);
            // if (paymentInfo == null) {
            //     paymentInfo = new PaymentInfo();
            //     BeanUtils.copyProperties(paymentInfoForm, paymentInfo);
            //     paymentInfo.setPaymentStatus(0);
            //     paymentInfoMapper.insert(paymentInfo);
            // }
            //
            // // 创建微信支付使用对象
            // JsapiServiceExtension service = new JsapiServiceExtension.Builder().config(rsaAutoCertificateConfig).build();
            //
            // // 封装微信支付参数
            // PrepayRequest request = new PrepayRequest();
            // Amount amount = new Amount();
            // amount.setTotal(paymentInfoForm.getAmount().multiply(new BigDecimal(100)).intValue());
            // request.setAmount(amount);
            // request.setSpAppid(wxPayV3Properties.getAppid());
            // request.setSpMchid(wxPayV3Properties.getMerchantId());
            // String description = paymentInfo.getContent();
            // if (description.length() > 127) {
            //     description = description.substring(0, 127);
            // }
            // request.setDescription(description);
            // request.setNotifyUrl(wxPayV3Properties.getNotifyUrl());
            // request.setOutTradeNo(paymentInfo.getOrderNo());
            //
            // // 获取用户信息
            // Payer payer = new Payer();
            // payer.setSpOpenid(paymentInfoForm.getCustomerOpenId());
            // request.setPayer(payer);
            //
            // // 是否指定分账 不指定不能分账
            // SettleInfo settleInfo = new SettleInfo();
            // settleInfo.setProfitSharing(true);
            // request.setSettleInfo(settleInfo);
            //
            // // 调用微信支付
            // PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(request, wxPayV3Properties.getAppid());

            WxPrepayVo wxPrepayVo = new WxPrepayVo();
            // BeanUtils.copyProperties(response, wxPrepayVo);
            // wxPrepayVo.setTimeStamp(response.getTimeStamp());
            wxPrepayVo.setTimeStamp(String.valueOf(System.currentTimeMillis()));

            return wxPrepayVo;
        } catch (Exception e) {
            log.warn("调用微信支付发生异常", e);
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public Boolean queryPayStatus(String orderId) {
        // 创建微信操作对象
        // JsapiServiceExtension service =
        //         new JsapiServiceExtension.Builder().config(rsaAutoCertificateConfig).build();
        //
        // // 封装查询支付状态需要参数
        // QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
        // queryRequest.setSpMchid(wxPayV3Properties.getMerchantId());
        // queryRequest.setOutTradeNo(orderNo);

        // 调用微信查询
        // Transaction transaction = service.queryOrderByOutTradeNo(queryRequest);
        // TODO: 2024/6/27 为了测试通过 此处手动赋值订单号 流水号 默认支付成功 直接进行支付完成后的逻辑处理

        Result<OrderInfo> orderInfo = orderInfoFeignClient.getOrderInfo(Long.valueOf(orderId));

        Transaction transaction = new Transaction();
        transaction.setOutTradeNo(orderInfo.getData().getOrderNo());
        transaction.setTransactionId(IdUtil.simpleUUID());
        transaction.setTradeState(Transaction.TradeStateEnum.SUCCESS);
        log.warn("默认微信支付成功，支付完成参数：{}", JSON.toJSONString(transaction));


        // 查询返回结果
        if (transaction != null
                && transaction.getTradeState() == Transaction.TradeStateEnum.SUCCESS) {
            // 如果支付成功 进行支付后处理逻辑
            this.handlePayment(transaction);

            return true;
        }
        return false;
    }

    @Override
    public void wxnotify(HttpServletRequest request) {
        // // 回调通知的验签与解密
        // // 从request头信息获取参数
        // // HTTP 头 Wechatpay-Signature
        // // HTTP 头 Wechatpay-Nonce
        // // HTTP 头 Wechatpay-Timestamp
        // // HTTP 头 Wechatpay-Serial
        // // HTTP 头 Wechatpay-Signature-Type
        // // HTTP 请求体 body。切记使用原始报文，不要用 JSON 对象序列化后的字符串，避免验签的 body 和原文不一致。
        // String wechatPaySerial = request.getHeader("Wechatpay-Serial");
        // String nonce = request.getHeader("Wechatpay-Nonce");
        // String timestamp = request.getHeader("Wechatpay-Timestamp");
        // String signature = request.getHeader("Wechatpay-Signature");
        // String requestBody = RequestUtils.readData(request);
        // //
        // // // 2.构造 RequestParam
        // RequestParam requestParam = new RequestParam.Builder()
        //         .serialNumber(wechatPaySerial)
        //         .nonce(nonce)
        //         .signature(signature)
        //         .timestamp(timestamp)
        //         .body(requestBody)
        //         .build();
        // //
        // // // 3.初始化 NotificationParser
        // NotificationParser parser = new NotificationParser(rsaAutoCertificateConfig);
        // // // 4.以支付通知回调为例，验签、解密并转换成 Transaction
        // Transaction transaction = parser.parse(requestParam, Transaction.class);
        //
        // if (null != transaction && transaction.getTradeState() == Transaction.TradeStateEnum.SUCCESS) {
        //     // 5.处理支付业务
        //     this.handlePayment(transaction);
        // }

        // TODO: 2024/6/27 此方法没有用 默认支付成功
    }

    /**
     * 进行支付后处理逻辑
     *
     * @param transaction
     */
    public void handlePayment(Transaction transaction) {
        // 更新支付记录 状态修改为 已支付
        String orderNo = transaction.getOutTradeNo();
        log.info("进行订单：{} 的支付完成后处理逻辑", orderNo);
        // 根据订单编号查询支付记录
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentInfo::getOrderNo, orderNo);
        PaymentInfo paymentInfo = paymentInfoMapper.selectOne(wrapper);
        // 如果已经支付 不需要更新
        if (paymentInfo.getPaymentStatus() == 1) {
            log.warn("该订单已经是已支付状态，订单号：{}", orderNo);
            return;
        }
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOrderNo(transaction.getOutTradeNo());
        paymentInfo.setTransactionId(transaction.getTransactionId());
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(transaction));
        paymentInfoMapper.updateById(paymentInfo);

        // 发送mq消息 传递 订单号 完成后续处理
        rabbitService.sendMessage(MqConst.EXCHANGE_ORDER,
                MqConst.ROUTING_PAY_SUCCESS,
                orderNo);
    }

    @GlobalTransactional
    @Override
    public void handleOrder(String orderNo) {
        // 更改订单支付状态
        orderInfoFeignClient.updateOrderPayStatus(orderNo);

        // 处理系统奖励，打入司机账户
        OrderRewardVo orderRewardVo = orderInfoFeignClient.getOrderRewardFee(orderNo).getData();
        log.info("调用远程service-order服务getOrderRewardFee结果：{}", JSON.toJSONString(orderRewardVo));


        if (null != orderRewardVo.getRewardFee() && orderRewardVo.getRewardFee().doubleValue() > 0) {
            TransferForm transferForm = new TransferForm();
            transferForm.setTradeNo(orderNo);
            transferForm.setTradeType(TradeType.REWARD.getType());
            transferForm.setContent(TradeType.REWARD.getContent());
            transferForm.setAmount(orderRewardVo.getRewardFee());
            transferForm.setDriverId(orderRewardVo.getDriverId());
            driverAccountFeignClient.transfer(transferForm);
        }

        // 分账处理
        OrderProfitsharingVo orderProfitsharingVo = orderInfoFeignClient.getOrderProfitsharing(orderRewardVo.getOrderId()).getData();
        if (orderProfitsharingVo != null) {
            log.info("订单：{}进行处理分账", orderRewardVo.getOrderId());
            // 封装分账参数对象
            ProfitsharingForm profitsharingForm = new ProfitsharingForm();
            profitsharingForm.setOrderNo(orderNo);
            profitsharingForm.setAmount(orderProfitsharingVo.getDriverIncome());
            profitsharingForm.setDriverId(orderRewardVo.getDriverId());
            // 分账有延迟，支付成功后最少2分钟执行分账申请
            rabbitService.sendDelayMessage(MqConst.EXCHANGE_PROFITSHARING, MqConst.ROUTING_PROFITSHARING, JSON.toJSONString(profitsharingForm), SystemConstant.PROFITSHARING_DELAY_TIME);
        }

    }
}
