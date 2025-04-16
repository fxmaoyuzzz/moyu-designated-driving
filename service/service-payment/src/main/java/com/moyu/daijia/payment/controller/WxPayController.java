package com.moyu.daijia.payment.controller;

import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.payment.PaymentInfo;
import com.moyu.daijia.model.form.payment.PaymentInfoForm;
import com.moyu.daijia.model.vo.payment.WxPrepayVo;
import com.moyu.daijia.payment.service.WxPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "微信支付接口")
@RestController
@RequestMapping("payment/wxPay")
@Slf4j
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;

    @Operation(summary = "创建微信支付")
    @PostMapping("/createWxPayment")
    public Result<WxPrepayVo> createWxPayment(@RequestBody PaymentInfoForm paymentInfoForm) {
        log.info("调用WxPayController.createWxPayment接口SUCCESS，入参：{}", paymentInfoForm);
        return Result.ok(wxPayService.createWxPayment(paymentInfoForm));
    }

    @Operation(summary = "支付状态查询")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable String orderNo) {
        log.info("调用WxPayController.queryPayStatus接口SUCCESS，入参：{}", orderNo);

        return Result.ok(wxPayService.queryPayStatus(orderNo));
    }

    @Operation(summary = "微信支付异步通知接口")
    @PostMapping("/notify")
    public Map<String, Object> notify(HttpServletRequest request) {
        log.info("调用WxPayController.notify接口SUCCESS");
        try {
            wxPayService.wxnotify(request);

            // 返回成功
            Map<String, Object> result = new HashMap<>();
            result.put("code", "SUCCESS");
            result.put("message", "成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回失败
        Map<String, Object> result = new HashMap<>();
        result.put("code", "FAIL");
        result.put("message", "失败");
        return result;
    }


    @Operation(summary = "支付时同步保存支付信息")
    @PostMapping("/savePaymentInfo")
    public Result<Boolean> savePaymentInfo(@RequestBody PaymentInfo paymentInfo) {
        log.info("调用WxPayController.savePaymentInfo接口SUCCESS，入参：{}", JSON.toJSONString(paymentInfo));
        wxPayService.savePaymentInfo(paymentInfo);
        return Result.ok(true);
    }

}