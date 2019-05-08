package com.demo.api.commons.entity;

import com.demo.api.commons.constants.GlobalResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Created by wanghw on 2019-02-22.
 */
@Slf4j
@ApiModel("返回结果")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Response<T> implements Serializable {
    /**
     * 返回码
     */
    @ApiModelProperty("状态码")
    private String code;

    @ApiModelProperty(hidden = true)
    private String message;
    /**
     * 返回内容对象
     */
    @ApiModelProperty("返回内容对象")
    private T data;

    public Response(){
        this.code = GlobalResult.SUCCESS;
    }
    public Response(T t) {
        this.code = GlobalResult.SUCCESS;
        this.data = t;
    }
    public Response(String message) {
        this.message = message;
    }
    public Response(String code,String message) {
        this.code = code;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public Response setData(T data) {
        this.data = data;
        return this;
    }
}
