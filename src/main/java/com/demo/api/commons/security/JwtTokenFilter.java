package com.demo.api.commons.security;

/**
 * Created by wanghw on 2019-03-21.
 */

import com.alibaba.fastjson.JSON;
import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
            SecurityContextHolder.clearContext();
            httpServletResponse.getWriter().print(JSON.toJSONString(new Response(GlobalResult.ACCESS_DENIED)));
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}