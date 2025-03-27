package com.moyu.daijia.customer.service;

import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.coupon.AvailableCouponVo;
import com.moyu.daijia.model.vo.coupon.NoReceiveCouponVo;
import com.moyu.daijia.model.vo.coupon.NoUseCouponVo;
import com.moyu.daijia.model.vo.coupon.UsedCouponVo;

import java.util.List;

public interface CouponService {


    /**
     * 查询未领取优惠券分页列表
     *
     * @param customerId
     * @param page
     * @param limit
     * @return
     */
    PageVo<NoReceiveCouponVo> findNoReceivePage(Long customerId, Long page, Long limit);

    /**
     * 查询未使用优惠券分页列表
     *
     * @param customerId
     * @param page
     * @param limit
     * @return
     */
    PageVo<NoUseCouponVo> findNoUsePage(Long customerId, Long page, Long limit);

    /**
     * 查询已使用优惠券分页列表
     *
     * @param customerId
     * @param page
     * @param limit
     * @return
     */
    PageVo<UsedCouponVo> findUsedPage(Long customerId, Long page, Long limit);

    /**
     * 领取优惠券
     *
     * @param customerId
     * @param couponId
     * @return
     */
    Boolean receive(Long customerId, Long couponId);

    /**
     * 获取未使用的最佳优惠券信息
     *
     * @param customerId
     * @param orderId
     * @return
     */
    List<AvailableCouponVo> findAvailableCoupon(Long customerId, Long orderId);
}
