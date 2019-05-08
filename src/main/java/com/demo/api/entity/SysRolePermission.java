package com.demo.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.demo.api.commons.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色权限表
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysRolePermission对象", description="系统角色权限表")
public class SysRolePermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long roleId;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long permissionId;

    @ApiModelProperty(value = "状态 1正常 2删除")
    private Integer status;

    @ApiModelProperty(value = "操作人ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long operationId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作人名字")
    @TableField(exist = false)
    private String operationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色名字")
    private String roleName;

    @TableField(exist = false)
    @ApiModelProperty(value = "权限名字")
    private String permissionName;

    @ApiModelProperty(value = "查询条件:开始时间")
    private transient LocalDateTime startDate;

    @ApiModelProperty(value = "查询条件:结束时间")
    private transient LocalDateTime endDate;

}
