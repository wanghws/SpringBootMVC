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
 * 系统角色表
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="SysRole对象", description="系统角色表")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "部门ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long officeId;

    @ApiModelProperty(value = "备注")
    private String remark;

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

    private transient Long userId;

    @ApiModelProperty(value = "操作人名字")
    @TableField(exist = false)
    private String operationName;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名字")
    private String officeName;

    @ApiModelProperty(value = "查询条件:开始时间")
    private transient LocalDateTime startDate;

    @ApiModelProperty(value = "查询条件:结束时间")
    private transient LocalDateTime endDate;
}
