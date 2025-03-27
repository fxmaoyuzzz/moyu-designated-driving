package com.moyu.daijia.coupon.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.daijia.model.entity.coupon.CouponInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyu.daijia.model.vo.coupon.NoReceiveCouponVo;
import com.moyu.daijia.model.vo.coupon.NoUseCouponVo;
import com.moyu.daijia.model.vo.coupon.UsedCouponVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {

    IPage<NoReceiveCouponVo> findNoReceivePage(Page<CouponInfo> pageParam, @Param("customerId") Long customerId);

    IPage<NoUseCouponVo> findNoUsePage(Page<CouponInfo> pageParam, @Param("customerId") Long customerId);

    IPage<UsedCouponVo> findUsedPage(Page<CouponInfo> pageParam, @Param("customerId") Long customerId);

    int updateReceiveCount(@Param("couponId") Long couponId);

    List<NoUseCouponVo> findNoUseList(Long customerId);

    int updateUseCount(@Param("id") Long id);
}
