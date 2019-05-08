package com.demo.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    Page<SysPermission> findPermissionPage(Page<SysPermission> page,
                                           @Param("sysPermission") SysPermission sysPermission);
}
