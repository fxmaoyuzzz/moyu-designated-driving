package com.moyu.daijia.security.fillter;


import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.common.util.IpUtil;
import com.moyu.daijia.common.util.ResponseUtil;
import com.moyu.daijia.model.entity.system.SysLoginLog;
import com.moyu.daijia.model.vo.system.LoginVo;
import com.moyu.daijia.security.custom.CustomUser;
import com.moyu.daijia.system.client.SysLoginLogFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 登录过滤器，继承UsernamePasswordAuthenticationFilter，对用户名密码进行登录校验
 * </p>
 *
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private RedisTemplate redisTemplate;
    private SysLoginLogFeignClient sysLoginLogFeignClient;

    private String ADMIN_LOGIN_KEY_PREFIX = "admin:login:";
    private int ADMIN_LOGIN_KEY_TIMEOUT = 60 * 60 * 24 * 100;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate, SysLoginLogFeignClient sysLoginLogFeignClient) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/securityLogin/login","POST"));
        this.redisTemplate = redisTemplate;
        this.sysLoginLogFeignClient = sysLoginLogFeignClient;
    }

    /**
     * 登录认证
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            LoginVo loginVo = new ObjectMapper().readValue(req.getInputStream(), LoginVo.class);

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 登录成功
     * @param request
     * @param response
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(ADMIN_LOGIN_KEY_PREFIX+token, customUser.getSysUser(), ADMIN_LOGIN_KEY_TIMEOUT, TimeUnit.SECONDS);

        //保存权限数据
        //redisTemplate.boundHashOps("admin:auth").put(customUser.getUsername(), customUser.getAuthorities());

        //记录日志
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(customUser.getUsername());
        sysLoginLog.setStatus(1);
        sysLoginLog.setIpaddr(IpUtil.getIpAddress(request));
        sysLoginLog.setMsg("登录成功");
        sysLoginLogFeignClient.recordLoginLog(sysLoginLog);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
    }

    /**
     * 登录失败
     * @param request
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {
        if(e.getCause() instanceof RuntimeException) {
            ResponseUtil.out(response, Result.build(null, 204, e.getMessage()));
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
        }
    }
}
