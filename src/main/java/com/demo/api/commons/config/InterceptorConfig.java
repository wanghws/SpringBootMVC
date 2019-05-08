package com.demo.api.commons.config;

import com.demo.api.commons.interceptor.AuthInterceptor;
import com.demo.api.commons.interceptor.LoginInterceptor;
import com.demo.api.commons.interceptor.SysUserArgumentResolver;
import com.demo.api.commons.interceptor.UserTokenArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wanghw on 2019-02-22.
 */
@Slf4j
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;
    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //登录拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .order(1)
                .excludePathPatterns(excludeArray);

        //后台登录拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/manage/**")
                .order(2)
                .excludePathPatterns(
                        "/manage/account/login",
                        "/manage/account/logout");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ServletWebArgumentResolverAdapter(new UserTokenArgumentResolver()));
        resolvers.add(new ServletWebArgumentResolverAdapter(new SysUserArgumentResolver()));
    }

    private String[] excludeArray = {
            "/status",
            "/*.ico",
            "/login",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/**",
            "/user/register",
            "/user/login",
            "/manage/**"
    };

}
