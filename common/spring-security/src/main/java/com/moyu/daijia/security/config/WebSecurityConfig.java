package com.moyu.daijia.security.config;

import com.moyu.daijia.security.custom.CustomMd5PasswordEncoder;
import com.moyu.daijia.security.fillter.TokenAuthenticationFilter;
import com.moyu.daijia.security.fillter.TokenLoginFilter;
import com.moyu.daijia.system.client.SysLoginLogFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomMd5PasswordEncoder customMd5PasswordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SysLoginLogFeignClient sysLoginLogFeignClient;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // 创建一个用户认证提供者
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 设置用户相信信息，可以从数据库中读取、或者缓存、或者配置文件
        authProvider.setUserDetailsService(userDetailsService);
        // 设置加密机制，若想要尝试对用户进行身份验证，我们需要知道使用的是什么编码
        authProvider.setPasswordEncoder(customMd5PasswordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //禁用csrf(防止跨站请求伪造攻击)
                .csrf()
                .disable()
                // 设置白名单
                .authorizeHttpRequests()
                //swg相关
                .requestMatchers("/favicon.ico","/swagger-resources/**", "/webjars/**", "/v3/**", "/doc.html").permitAll()
                //用户登录相关接口
                .requestMatchers("/securityLogin/login").permitAll()
                // 对于其他任何请求，都保护起来
                .anyRequest().authenticated()
                .and()
                // 禁用缓存
                .sessionManagement()
                // 使用无状态session，即不使用session缓存数据
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 添加身份验证
                .and()
                // TODO 添加身份验证1
                .authenticationProvider(authenticationProvider())
                // 添加token过滤器
                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new TokenLoginFilter(authenticationManager(authenticationConfiguration), redisTemplate, sysLoginLogFeignClient))
                ;


        return http.build();
    }

}