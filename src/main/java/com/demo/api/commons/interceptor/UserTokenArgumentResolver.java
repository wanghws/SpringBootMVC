package com.demo.api.commons.interceptor;

import com.demo.api.entity.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wanghw on 2019-02-27.
 */
public class UserTokenArgumentResolver  implements WebArgumentResolver {
    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        if(null == methodParameter.getParameterType() || ! methodParameter.getParameterType().equals(User.class)) {
            return UNRESOLVED;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        return httpServletRequest.getAttribute("sessionUser");
    }
}