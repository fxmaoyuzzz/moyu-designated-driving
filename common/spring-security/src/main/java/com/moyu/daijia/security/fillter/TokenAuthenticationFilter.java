package com.moyu.daijia.security.fillter;


import com.alibaba.fastjson.JSON;
import com.moyu.daijia.common.result.Result;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.common.util.AuthContextHolder;
import com.moyu.daijia.common.util.ResponseUtil;
import com.moyu.daijia.model.entity.system.SysUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 身份验证过滤器
 * </p>
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    private String ADMIN_LOGIN_KEY_PREFIX = "admin:login:";
    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //如果是登录接口或非admin大头的直接放行
        String uri = request.getRequestURI();
        if(antPathMatcher.match("/securityLogin/login", uri) ||
                antPathMatcher.match("/swagger-resources/**", uri) ||
                antPathMatcher.match("/webjars/**", uri) ||
                antPathMatcher.match("/v3/**", uri) ||
                antPathMatcher.match("/doc.html", uri) ||
                antPathMatcher.match("/favicon.ico", uri)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (StringUtils.hasText(token)) {
            SysUser sysUser = (SysUser)redisTemplate.opsForValue().get(ADMIN_LOGIN_KEY_PREFIX+token);
            logger.info("sysUser:"+JSON.toJSONString(sysUser));
            if (null != sysUser) {
                AuthContextHolder.setUserId(sysUser.getId());

                if (null != sysUser.getUserPermsList() && sysUser.getUserPermsList().size() > 0) {
                    List<SimpleGrantedAuthority> authorities = sysUser.getUserPermsList().stream().filter(code -> StringUtils.hasText(code.trim())).map(code -> new SimpleGrantedAuthority(code.trim())).collect(Collectors.toList());
                    return new UsernamePasswordAuthenticationToken(sysUser.getUsername(), null, authorities);
                } else {
                    return new UsernamePasswordAuthenticationToken(sysUser.getUsername(), null, new ArrayList<>());
                }
            }
        }
        return null;
    }
}
