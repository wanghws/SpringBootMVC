package com.demo.api.commons.interceptor;

import com.demo.api.entity.SysUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wanghw on 2019-02-27.
 */
public class SysUserArgumentResolver implements WebArgumentResolver {
    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        if(null == methodParameter.getParameterType() || ! methodParameter.getParameterType().equals(SysUser.class)) {
            return UNRESOLVED;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        return httpServletRequest.getAttribute("sysUser");
    }
}