package com.demo.api.service.impl;

import com.demo.api.entity.SysUser;
import com.demo.api.service.IAuthUserDetailsService;
import com.demo.api.service.ISysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghw on 2019-03-21.
 */
@Service
public class AuthUserDetailsServiceImpl implements IAuthUserDetailsService {
    @Resource
    private ISysUserService sysUserService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserByLoginName(s);
        if(null == sysUser){
            throw new UsernameNotFoundException(String.format("未找到名字为'%s'.",s));
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String permission:sysUser.getRoles()){
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return new User(sysUser.getLoginName(),sysUser.getPassword(),authorities);
    }
}
