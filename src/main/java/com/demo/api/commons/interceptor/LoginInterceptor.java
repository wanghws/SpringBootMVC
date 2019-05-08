package com.demo.api.commons.interceptor;

import com.alibaba.fastjson.JSON;
import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.entity.Response;
import com.demo.api.commons.security.JwtTokenProvider;
import com.demo.api.entity.SysUser;
import com.demo.api.service.ISysUserService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        try{

            String token = request.getHeader("x-authorization-bms");
            if (Strings.isNullOrEmpty(token)){
                response.getWriter().print(JSON.toJSONString(new Response(GlobalResult.NOT_LOGIN)));
                return false;
            }

            String name = jwtTokenProvider.getUsername(token);
            if (Strings.isNullOrEmpty(name)){
                response.getWriter().print(JSON.toJSONString(new Response(GlobalResult.NOT_LOGIN)));
                return false;
            }
            SysUser user = sysUserService.getUserByLoginName(name);
            if (null == user ||  user.getId() == null){
                response.getWriter().print(JSON.toJSONString(new Response(GlobalResult.NOT_LOGIN)));
                return false;
            }

            request.setAttribute("sysUser",user);
            return true;
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return false;
        }
    }
}
