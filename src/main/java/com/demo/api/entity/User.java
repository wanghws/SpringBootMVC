package com.demo.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.demo.api.commons.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author wanghw
 * @since 2019-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value="User对象", description="用户表")
public class User extends BaseEntity {

    public User(){};

    public User(Long id){
        this.id = id;
    }

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "状态 0:禁用 1:正常 2:锁定")
    private Integer status;

    @JsonIgnore
    @ApiModelProperty(value = "密码时间")
    private LocalDateTime passwordTime;

    @JsonIgnore
    @ApiModelProperty(value = "密码ip")
    private Integer passwordIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "注册时间")
    private LocalDateTime registerTime;

    @JsonIgnore
    @ApiModelProperty(value = "注册IP")
    private Integer registerIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "登录IP")
    private Integer loginIp;
}
