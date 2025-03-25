package com.moyu.daijia.common.login;

import com.moyu.daijia.common.constant.RedisConstant;
import com.moyu.daijia.common.execption.MoyuException;
import com.moyu.daijia.common.result.ResultCodeEnum;
import com.moyu.daijia.common.util.AuthContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author fxmao
 * @date 2024-06-25 15:30
 */

@Component
@Aspect
public class GlobalLoginAspect {


    @Autowired
    private RedisTemplate redisTemplate;

    @Around("execution(* com.moyu.daijia.*.controller.*.*(..)) && @annotation(globalLogin)")
    public Object login(ProceedingJoinPoint proceedingJoinPoint, GlobalLogin globalLogin) throws Throwable {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) attributes;
        HttpServletRequest request = sra.getRequest();

        // 从请求头获取token
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            throw new MoyuException(ResultCodeEnum.LOGIN_AUTH);
        }

        //查询redis 把用户id放到ThreadLocal里面
        String customerId = (String) redisTemplate.opsForValue()
                .get(RedisConstant.USER_LOGIN_KEY_PREFIX + token);

        if (StringUtils.hasText(customerId)) {
            AuthContextHolder.setUserId(Long.parseLong(customerId));
        }

        // 6 执行业务方法
        return proceedingJoinPoint.proceed();
    }
}
