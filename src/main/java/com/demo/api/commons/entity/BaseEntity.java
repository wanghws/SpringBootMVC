package com.demo.api.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wanghw on 2019-02-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BaseEntity implements Serializable {
    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(value = "ID",dataType = "java.lang.String")
    @TableId(type = IdType.ID_WORKER)
    protected Long id;
}
