package com.demo.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.service.BaseServiceImpl;
import com.demo.api.entity.SysPermission;
import com.demo.api.entity.SysRole;
import com.demo.api.entity.SysRolePermission;
import com.demo.api.entity.SysUser;
import com.demo.api.mapper.SysRoleMapper;
import com.demo.api.service.ISysRolePermissionService;
import com.demo.api.service.ISysRoleService;
import com.google.common.collect.Lists;
import me.chanjar.weixin.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private ISysRolePermissionService sysRolePermissionService;

    public List<SysRole> findRoleByUser(SysRole sysRole){
        return roleMapper.findRoleByUser(sysRole);
    }
    public List<SysPermission> findRolePermissionsByUser(SysPermission sysPermission){
        return roleMapper.findRolePermissionsByUser(sysPermission);
    }

    public Page<SysRole> findRolePage(Page<SysRole> sysRolePage, SysRole sysRole) {
        return roleMapper.findRolePage(sysRolePage,sysRole);
    }

    public void updateRole(SysRole sysRole, SysUser sysUser) {
        SysRole update = new SysRole();
        boolean flag = false;
        if (StringUtils.isNotBlank(sysRole.getName())){
            update.setName(sysRole.getName());
            flag = true;
        }
        if (StringUtils.isNotBlank(sysRole.getRemark())) {
            update.setRemark(sysRole.getRemark());
            flag = true;
        }
        if (sysRole.getOfficeId() != null){
            update.setOfficeId(sysRole.getOfficeId());
            flag = true;
        }
        if (flag){
            update.setId(sysRole.getId());
            update.setOperationId(sysUser.getId());
            update.setUpdateTime(LocalDateTime.now());
            updateById(update);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long roleId, Integer status, SysUser sysUser) {
        SysRole update = new SysRole();
        update.setId(roleId);
        update.setStatus(status);
        update.setUpdateTime(LocalDateTime.now());
        update.setOperationId(sysUser.getId());
        updateById(update);
        List<SysRolePermission> list = sysRolePermissionService.lambdaQuery().eq(SysRolePermission::getRoleId, roleId).list();
        if (!CollectionUtils.isEmpty(list)) {
            List<SysRolePermission> permissions = Lists.newArrayList();
            list.forEach(sysRolePermission -> {
                if (status.equals(sysRolePermission.getStatus())) return;
                SysRolePermission updatePermission = new SysRolePermission();
                updatePermission.setId(sysRolePermission.getId());
                updatePermission.setStatus(status);
                updatePermission.setUpdateTime(LocalDateTime.now());
                updatePermission.setOperationId(sysUser.getId());
                permissions.add(updatePermission);
            });
            if (!CollectionUtils.isEmpty(permissions)){
                sysRolePermissionService.updateBatchById(permissions);
            }
        }
    }

    public Page<SysRolePermission> findRolePermissionPage(Page<SysRolePermission> permissionPage, SysRolePermission sysRolePermission) {
        return roleMapper.findRolePermissionPage(permissionPage,sysRolePermission);
    }

    public List<SysRolePermission> findRolePermissionsByRole(Long roleId) {
        return roleMapper.findRolePermissionsByRole(roleId);
    }
}
