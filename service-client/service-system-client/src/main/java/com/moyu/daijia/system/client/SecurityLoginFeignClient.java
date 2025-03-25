package com.moyu.daijia.system.client;

import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.model.entity.system.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品列表API接口
 * </p>
 *
 * @author qy
 */
@FeignClient(value = "service-system")
public interface SecurityLoginFeignClient {

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    @GetMapping("/securityLogin/getByUsername/{username}")
    Result<SysUser> getByUsername(@PathVariable("username") String username);

    /**
     * 获取用户按钮权限
     *
     * @param userId
     * @return
     */
    @GetMapping("/securityLogin/findUserPermsList/{userId}")
    Result<List<String>> findUserPermsList(@PathVariable("userId") Long userId);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/securityLogin/getUserInfo/{userId}")
    Result<Map<String, Object>> getUserInfo(@PathVariable("userId") Long userId);
}