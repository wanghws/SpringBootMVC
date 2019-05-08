package com.demo.api.service;

import com.demo.api.commons.service.IBaseService;
import com.demo.api.entity.SysRolePermission;
import com.demo.api.entity.SysUser;

/**
 * <p>
 * 系统角色权限表 服务类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface ISysRolePermissionService extends IBaseService<SysRolePermission> {

    void updateStatus(Long rolePermissionId, Integer status, SysUser sysUser);
}
