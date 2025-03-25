package com.moyu.daijia.common.constant;

public class MqConst {


    public static final String EXCHANGE_ORDER = "daijia.order";
    public static final String ROUTING_PAY_SUCCESS = "daijia.pay.success";
    public static final String ROUTING_PROFITSHARING_SUCCESS = "daijia.profitsharing.success";
    public static final String QUEUE_PAY_SUCCESS = "daijia.pay.success";
    public static final String QUEUE_PROFITSHARING_SUCCESS = "daijia.profitsharing.success";


    //取消订单延迟消息
    public static final String EXCHANGE_CANCEL_ORDER = "daijia.cancel.order";
    public static final String ROUTING_CANCEL_ORDER = "daijia.cancel.order";
    public static final String QUEUE_CANCEL_ORDER = "daijia.cancel.order";

    //分账延迟消息
    public static final String EXCHANGE_PROFITSHARING = "daijia.profitsharing";
    public static final String ROUTING_PROFITSHARING = "daijia.profitsharing";
    public static final String QUEUE_PROFITSHARING  = "daijia.profitsharing";

}
