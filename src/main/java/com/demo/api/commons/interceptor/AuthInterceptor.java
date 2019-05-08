package com.demo.api.commons.interceptor;

import com.alibaba.fastjson.JSON;
import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.entity.Response;
import com.demo.api.entity.User;
import com.demo.api.service.IUserService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        try{

            String token = request.getHeader("x-authorization");
            if (Strings.isNullOrEmpty(token)){
                response.getWriter().print(JSON.toJSONString(new Response(GlobalResult.NOT_LOGIN)));
                response.flushBuffer();
                return false;
            }

            User user = userService.validateToken(token);
            if (null == user ||  user.getId() == null){
                response.getWriter().print(JSON.toJSONString(new Response(GlobalResult.NOT_LOGIN)));
                response.flushBuffer();
                return false;
            }

            request.setAttribute("sessionUser",user);
            return true;
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return false;
        }
    }
}
