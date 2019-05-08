package com.demo.api.commons.filter;

import com.alibaba.fastjson.JSON;
import com.demo.api.commons.security.JwtTokenProvider;
import com.demo.api.entity.User;
import com.demo.api.service.IUserService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghw on 2019-02-22.
 */
@Profile({"test","production"})
@Slf4j
@Component
public class AccessFilter implements Filter {
    @Resource
    private IUserService userService;
    @Resource
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest)servletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse)servletResponse);
        filterChain.doFilter(requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();

        new LogThread((HttpServletRequest)servletRequest,(HttpServletResponse)servletResponse,responseWrapper.getStatusCode(),(System.currentTimeMillis() - startTime)+0.0).run();
    }

    @Override
    public void destroy() {

    }


    public class LogThread extends Thread{

        private HttpServletRequest request;
        private HttpServletResponse response;
        private Integer httpCode;
        private Double duration;

        LogThread(HttpServletRequest request,HttpServletResponse response,Integer httpCode,Double duration){
            this.request = request;
            this.response  = response;
            this.duration = duration;
            this.httpCode = httpCode;
        }

        @Override
        public void run() {
            //MDC.put("ip", host);
            String ua = request.getHeader("user-agent");
            if (null!=ua && ua.startsWith("kube-probe"))return;

            Map<String,Object> map = new HashMap<>();


            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);

                if (!Strings.isNullOrEmpty(key) && key.contains("x-authorization")){
                    User user = userService.validateToken(value);
                    if (null==user)continue;
                    map.put("userId", user.getId());
                    continue;
                }
                if (!Strings.isNullOrEmpty(key) && key.contains("x-authorization-mms")){
                    String loginName = jwtTokenProvider.getUsername(value);
                    if (null==loginName)continue;
                    map.put("loginName", loginName);
                    continue;
                }
                map.put(key, value);
            }
            String url = this.request.getRequestURI();
            map.put("url",url);

            Map requestMap = this.request.getParameterMap();

            map.putAll(requestMap);

            try{

                map.put("httpCode",this.httpCode);
                map.put("duration",(this.duration/1000));

                map.remove("cookie");
                map.remove("Cookie");
                map.remove("r");
                map.remove("origin");
                map.remove("connection");
                map.remove("upgrade-insecure-requests");
                map.remove("cookie");

                log.info(JSON.toJSONString(map));
            }catch(Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }
}
