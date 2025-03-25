package com.moyu.daijia.system.service.impl;//package com.moyu.daijia.system.service.impl;
//
//import com.moyu.daijia.model.system.SysUser;
//import com.moyu.daijia.security.custom.CustomUser;
//import com.moyu.daijia.system.service.SysMenuService;
//import com.moyu.daijia.system.service.SysUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//
//@Component
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private SysUserService sysUserService;
//
//    @Autowired
//    private SysMenuService sysMenuService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        SysUser sysUser = sysUserService.getByUsername(username);
//        if(null == sysUser) {
//            throw new UsernameNotFoundException("用户名不存在！");
//        }
//
//        if(!"admin".equals(sysUser.getUsername()) && sysUser.getStatus().intValue() == 0) {
//            throw new RuntimeException("账号已停用");
//        }
//        List<String> userPermsList = sysMenuService.findUserPermsList(sysUser.getId());
//        sysUser.setUserPermsList(userPermsList);
//        //List<SimpleGrantedAuthority> authorities = userPermsList.stream().filter(code -> StringUtils.hasText(code.trim())).map(code -> new SimpleGrantedAuthority(code.trim())).collect(Collectors.toList());
//        return new CustomUser(sysUser);
//    }
//}