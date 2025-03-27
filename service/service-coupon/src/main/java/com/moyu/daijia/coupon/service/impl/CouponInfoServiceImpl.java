package com.moyu.daijia.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.daijia.common.constant.RedisConstant;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.coupon.mapper.CouponInfoMapper;
import com.moyu.daijia.coupon.mapper.CustomerCouponMapper;
import com.moyu.daijia.coupon.service.CouponInfoService;
import com.moyu.daijia.model.entity.coupon.CouponInfo;
import com.moyu.daijia.model.entity.coupon.CustomerCoupon;
import com.moyu.daijia.model.form.coupon.UseCouponForm;
import com.moyu.daijia.model.vo.base.PageVo;
import com.moyu.daijia.model.vo.coupon.AvailableCouponVo;
import com.moyu.daijia.model.vo.coupon.NoReceiveCouponVo;
import com.moyu.daijia.model.vo.coupon.NoUseCouponVo;
import com.moyu.daijia.model.vo.coupon.UsedCouponVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {


    @Autowired
    private CouponInfoMapper couponInfoMapper;

    @Autowired
    private CustomerCouponMapper customerCouponMapper;

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public PageVo<NoReceiveCouponVo> findNoReceivePage(Page<CouponInfo> pageParam, Long customerId) {
        IPage<NoReceiveCouponVo> pageInfo = couponInfoMapper.findNoReceivePage(pageParam, customerId);
        return new PageVo(pageInfo.getRecords(), pageInfo.getPages(), pageInfo.getTotal());
    }

    @Override
    public PageVo<NoUseCouponVo> findNoUsePage(Page<CouponInfo> pageParam, Long customerId) {
        IPage<NoUseCouponVo> pageInfo = couponInfoMapper.findNoUsePage(pageParam, customerId);
        return new PageVo(pageInfo.getRecords(), pageInfo.getPages(), pageInfo.getTotal());
    }

    @Override
    public PageVo<UsedCouponVo> findUsedPage(Page<CouponInfo> pageParam, Long customerId) {
        IPage<UsedCouponVo> pageInfo = couponInfoMapper.findUsedPage(pageParam, customerId);
        return new PageVo(pageInfo.getRecords(), pageInfo.getPages(), pageInfo.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean receive(Long customerId, Long couponId) {
        CouponInfo couponInfo = this.getById(couponId);
        if (null == couponInfo) {
            log.warn("查询优惠券详情失败，优惠券id：{}", JSON.toJSONString(couponId));
            throw new MoyuException(ResultCodeEnum.DATA_ERROR);
        }
        if (couponInfo.getExpireTime().before(new Date())) {
            log.warn("当前优惠券已过期，优惠券id：{}", JSON.toJSONString(couponId));
            throw new MoyuException(ResultCodeEnum.COUPON_EXPIRE);
        }

        // 检查库存 发行数量 和 领取数量
        if (couponInfo.getPublishCount() != 0 && couponInfo.getReceiveCount().equals(couponInfo.getPublishCount())) {
            log.warn("当前优惠券库存不足，优惠券id：{}", JSON.toJSONString(couponId));
            throw new MoyuException(ResultCodeEnum.COUPON_LESS);
        }
        RLock lock = null;
        try {
            lock = redissonClient.getLock(RedisConstant.COUPON_LOCK + customerId);
            boolean flag = lock.tryLock(RedisConstant.COUPON_LOCK_WAIT_TIME,
                    RedisConstant.COUPON_LOCK_LEASE_TIME, TimeUnit.SECONDS);
            if (flag) {
                // 4 检查每个人限制领取数量
                if (couponInfo.getPerLimit() > 0) {
                    // 统计当前客户已经领取优惠卷数量
                    LambdaQueryWrapper<CustomerCoupon> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(CustomerCoupon::getCouponId, couponId);
                    wrapper.eq(CustomerCoupon::getCustomerId, customerId);
                    Long count = customerCouponMapper.selectCount(wrapper);
                    if (count >= couponInfo.getPerLimit()) {
                        log.warn("当前用户超出领取上限，优惠券id：{}", JSON.toJSONString(couponId));
                        throw new MoyuException(ResultCodeEnum.COUPON_USER_LIMIT);
                    }
                }
                // 领取优惠卷 更新领取数量
                int row = couponInfoMapper.updateReceiveCount(couponId);

                // 添加领取记录
                this.saveCustomerCoupon(customerId, couponId, couponInfo.getExpireTime());

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
        return true;
    }

    private void saveCustomerCoupon(Long customerId, Long couponId, Date expireTime) {
        CustomerCoupon customerCoupon = new CustomerCoupon();
        customerCoupon.setCustomerId(customerId);
        customerCoupon.setCouponId(couponId);
        customerCoupon.setStatus(1);
        customerCoupon.setReceiveTime(new Date());
        customerCoupon.setExpireTime(expireTime);
        customerCouponMapper.insert(customerCoupon);
    }

    @Override
    public List<AvailableCouponVo> findAvailableCoupon(Long customerId, BigDecimal orderAmount) {
        List<AvailableCouponVo> availableCouponVoList = new ArrayList<>();

        // 根据乘客id获取乘客已经领取但是没有使用的优惠卷列表
        List<NoUseCouponVo> list = couponInfoMapper.findNoUseList(customerId);

        // 遍历乘客未使用优惠卷列表 判断优惠卷类型 现金卷 和 折扣卷
        List<NoUseCouponVo> typeList =
                list.stream().filter(item -> item.getCouponType() == 1).collect(Collectors.toList());

        // 判断现金卷是否满足条件
        for (NoUseCouponVo noUseCouponVo : typeList) {
            // 判断使用门槛
            // 减免金额
            BigDecimal reduceAmount = noUseCouponVo.getAmount();
            // 无门槛 订单金额必须大于优惠减免金额
            if (noUseCouponVo.getConditionAmount().doubleValue() == 0
                    && orderAmount.subtract(reduceAmount).doubleValue() > 0) {
                availableCouponVoList.add(this.buildBestNoUseCouponVo(noUseCouponVo, reduceAmount));
            }

            // 有门槛 订单金额大于优惠门槛金额
            if (noUseCouponVo.getConditionAmount().doubleValue() > 0
                    && orderAmount.subtract(noUseCouponVo.getConditionAmount()).doubleValue() > 0) {
                availableCouponVoList.add(this.buildBestNoUseCouponVo(noUseCouponVo, reduceAmount));
            }
        }

        // 折扣卷 判断折扣卷是否满足条件
        List<NoUseCouponVo> typeList2 =
                list.stream().filter(item -> item.getCouponType() == 2).collect(Collectors.toList());
        for (NoUseCouponVo noUseCouponVo : typeList2) {
            // 折扣之后金额
            // 100 打8折  = 100 * 8 /10= 80
            BigDecimal discountAmount = orderAmount.multiply(noUseCouponVo.getDiscount())
                    .divide(new BigDecimal("10")).setScale(2, RoundingMode.HALF_UP);

            BigDecimal reduceAmount = orderAmount.subtract(discountAmount);
            // 无门槛
            if (noUseCouponVo.getConditionAmount().doubleValue() == 0) {
                availableCouponVoList.add(this.buildBestNoUseCouponVo(noUseCouponVo, reduceAmount));
            }
            // 有门槛 订单折扣后金额大于优惠券门槛金额
            if (noUseCouponVo.getConditionAmount().doubleValue() > 0
                    && discountAmount.subtract(noUseCouponVo.getConditionAmount()).doubleValue() > 0) {
                availableCouponVoList.add(this.buildBestNoUseCouponVo(noUseCouponVo, reduceAmount));
            }

        }

        // 把满足条件优惠卷根据金额排序
        if (!CollectionUtils.isEmpty(availableCouponVoList)) {
            Collections.sort(availableCouponVoList, new Comparator<AvailableCouponVo>() {
                @Override
                public int compare(AvailableCouponVo o1, AvailableCouponVo o2) {
                    return o1.getReduceAmount().compareTo(o2.getReduceAmount());
                }
            });
        }

        return availableCouponVoList;
    }

    private AvailableCouponVo buildBestNoUseCouponVo(NoUseCouponVo noUseCouponVo, BigDecimal reduceAmount) {
        AvailableCouponVo bestNoUseCouponVo = new AvailableCouponVo();
        BeanUtils.copyProperties(noUseCouponVo, bestNoUseCouponVo);
        bestNoUseCouponVo.setCouponId(noUseCouponVo.getId());
        bestNoUseCouponVo.setReduceAmount(reduceAmount);
        return bestNoUseCouponVo;
    }

    @Transactional(noRollbackFor = Exception.class)
    @Override
    public BigDecimal useCoupon(UseCouponForm useCouponForm) {
        // 获取乘客优惠券
        CustomerCoupon customerCoupon = customerCouponMapper.selectById(useCouponForm.getCustomerCouponId());
        if (null == customerCoupon) {
            throw new MoyuException(ResultCodeEnum.ARGUMENT_VALID_ERROR);
        }
        // 获取优惠券信息
        CouponInfo couponInfo = couponInfoMapper.selectById(customerCoupon.getCouponId());
        if (null == couponInfo) {
            throw new MoyuException(ResultCodeEnum.ARGUMENT_VALID_ERROR);
        }
        // 判断该优惠券是否为乘客所有
        if (customerCoupon.getCustomerId().longValue() != useCouponForm.getCustomerId().longValue()) {
            throw new MoyuException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        // 获取优惠券减免金额
        BigDecimal reduceAmount = null;
        if (couponInfo.getCouponType().intValue() == 1) {
            // 使用门槛判断
            // 无门槛 订单金额必须大于优惠券减免金额
            if (couponInfo.getConditionAmount().doubleValue() == 0 && useCouponForm.getOrderAmount().subtract(couponInfo.getAmount()).doubleValue() > 0) {
                // 减免金额
                reduceAmount = couponInfo.getAmount();
            }
            // 有门槛 订单金额大于优惠券门槛金额
            if (couponInfo.getConditionAmount().doubleValue() > 0 && useCouponForm.getOrderAmount().subtract(couponInfo.getConditionAmount()).doubleValue() > 0) {
                // 减免金额
                reduceAmount = couponInfo.getAmount();
            }
        } else {
            // 使用门槛判断
            // 订单折扣后金额
            BigDecimal discountOrderAmount = useCouponForm.getOrderAmount().multiply(couponInfo.getDiscount()).divide(new BigDecimal("10")).setScale(2, RoundingMode.HALF_UP);
            // 订单优惠金额
            // 无门槛
            if (couponInfo.getConditionAmount().doubleValue() == 0) {
                // 减免金额
                reduceAmount = useCouponForm.getOrderAmount().subtract(discountOrderAmount);
            }
            // 有门槛 订单折扣后金额大于优惠券门槛金额
            if (couponInfo.getConditionAmount().doubleValue() > 0 && discountOrderAmount.subtract(couponInfo.getConditionAmount()).doubleValue() > 0) {
                // 减免金额
                reduceAmount = useCouponForm.getOrderAmount().subtract(discountOrderAmount);
            }
        }
        if (reduceAmount.doubleValue() > 0) {
            int row = couponInfoMapper.updateUseCount(couponInfo.getId());
            if (row == 1) {
                CustomerCoupon updateCustomerCoupon = new CustomerCoupon();
                updateCustomerCoupon.setId(customerCoupon.getId());
                updateCustomerCoupon.setUsedTime(new Date());
                updateCustomerCoupon.setOrderId(useCouponForm.getOrderId());
                customerCouponMapper.updateById(updateCustomerCoupon);
                return reduceAmount;
            }
        }
        throw new MoyuException(ResultCodeEnum.DATA_ERROR);
    }
}
