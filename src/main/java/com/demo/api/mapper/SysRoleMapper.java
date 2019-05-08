package com.demo.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.entity.SysPermission;
import com.demo.api.entity.SysRole;
import com.demo.api.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<SysRole> findRoleByUser(SysRole sysRole);
    List<SysPermission> findRolePermissionsByUser(@Param("sysPermission") SysPermission sysPermission);

    Page<SysRole> findRolePage(Page<SysRole> sysRolePage,@Param("sysRole") SysRole sysRole);

    Page<SysRolePermission> findRolePermissionPage(Page<SysRolePermission> page, @Param("sysRolePermission") SysRolePermission sysRolePermission);

    List<SysRolePermission> findRolePermissionsByRole(@Param("roleId") Long roleId);
}
