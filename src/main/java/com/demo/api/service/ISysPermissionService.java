package com.demo.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.service.IBaseService;
import com.demo.api.entity.SysPermission;
import com.demo.api.entity.SysUser;

/**
 * <p>
 * 系统权限表 服务类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface ISysPermissionService extends IBaseService<SysPermission> {

    Page<SysPermission> findPermissionPage(Page<SysPermission> sysPermissionPage,SysPermission sysPermission);

    void updateStatus(Long id, Integer status,SysUser sysUser);
}
