package com.demo.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.service.IBaseService;
import com.demo.api.entity.SysPermission;
import com.demo.api.entity.SysRole;
import com.demo.api.entity.SysRolePermission;
import com.demo.api.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface ISysRoleService extends IBaseService<SysRole> {
    List<SysRole> findRoleByUser(SysRole sysRole);
    List<SysPermission> findRolePermissionsByUser(SysPermission sysPermission);

    Page<SysRole> findRolePage(Page<SysRole> sysRolePage, SysRole sysRole);
    void updateRole(SysRole update, SysUser sysUser);
    void updateStatus( Long roleId, Integer status, SysUser sysUser);
    Page<SysRolePermission> findRolePermissionPage(Page<SysRolePermission> permissionPage,SysRolePermission sysRolePermission);

    List<SysRolePermission> findRolePermissionsByRole(Long roleId);
}
