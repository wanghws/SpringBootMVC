package com.demo.api.commons.exception;

import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * Created by wanghw on 2019-02-22.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NullPointerException.class)
    public Response defaultNullPointerException(Exception e) {
        return new Response(GlobalResult.FAIL,e.getMessage());
    }
    @ExceptionHandler(value = Throwable.class)
    public Response defaultException(Exception e) {
        return new Response(GlobalResult.FAIL,e.getMessage());
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public Response accessDeniedException(Exception e) {
        return new Response(GlobalResult.ACCESS_DENIED,e.getMessage());
    }
    @ExceptionHandler(value = ServletException.class)
    public Response missingParameterException(Exception e) {
        //log(e);
        return new Response(GlobalResult.FAIL,e.getMessage());
    }

    /**
     * 表单验证错误
     * */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Response constraintViolationException(ValidationException exception) {
        if(exception instanceof ConstraintViolationException){
            ConstraintViolationException exs = (ConstraintViolationException) exception;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            if (!violations.isEmpty()){
                return new Response(violations.iterator().next().getMessage());
            }
        }
        return new Response(GlobalResult.FAIL, exception.getMessage());
    }

    /**
     * 业务逻辑错误
     * */
    @ExceptionHandler(value = SysExcetpion.class)
    public Response sysException(Exception e) {
        if (e instanceof SysExcetpion){
            return new Response(((SysExcetpion) e).getCode());
        }
        return defaultException(e);
    }
}
