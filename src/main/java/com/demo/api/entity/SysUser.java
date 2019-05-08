package com.demo.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.demo.api.commons.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysUser对象", description="系统用户表")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long officeId;

    @ApiModelProperty(value = "昵称")
    private String loginName;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "状态 0:禁用 1:正常 2:锁定")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "密码时间")
    private LocalDateTime passwordTime;

    @ApiModelProperty(value = "密码ip")
    private Integer passwordIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "登录IP")
    private Integer loginIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "注册时间")
    private LocalDateTime registerTime;

    @ApiModelProperty(value = "注册IP")
    private Integer registerIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名字")
    private String officeName;

    @TableField(exist = false)
    @ApiModelProperty(value = "权限路由")
    private Set<String> roles;

    @ApiModelProperty(value = "查询条件:注册开始时间")
    private transient LocalDateTime regStartDate;

    @ApiModelProperty(value = "查询条件:注册结束时间")
    private transient LocalDateTime regEndDate;

    @ApiModelProperty(value = "查询条件:登录开始时间")
    private transient LocalDateTime loginStartDate;

    @ApiModelProperty(value = "查询条件:登录结束时间")
    private transient LocalDateTime loginEndDate;

}
