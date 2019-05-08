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
import java.util.List;

/**
 * <p>
 * 系统权限表
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysPermission对象", description="系统权限表")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父权限ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "权限关键字")
    private String permission;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否显示 0显示1隐藏")
    private Integer hidden;

    @ApiModelProperty(value = "状态 1正常 2删除")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<SysPermission> permissionList;

    private transient Long userId;

    @ApiModelProperty(value = "操作人ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long operationId;

    @ApiModelProperty(value = "操作人名字")
    @TableField(exist = false)
    private String operationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "父权限名字")
    private String parentName;

    @ApiModelProperty(value = "查询条件:开始时间")
    private transient LocalDateTime startDate;

    @ApiModelProperty(value = "查询条件:结束时间")
    private transient LocalDateTime endDate;
}
