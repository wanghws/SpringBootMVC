package com.demo.api.service.impl;

import com.demo.api.commons.service.BaseServiceImpl;
import com.demo.api.entity.SysRolePermission;
import com.demo.api.entity.SysUser;
import com.demo.api.mapper.SysRolePermissionMapper;
import com.demo.api.service.ISysRolePermissionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色权限表 服务实现类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Service
public class SysRolePermissionServiceImpl extends BaseServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    public void updateStatus(Long rolePermissionId, Integer status, SysUser sysUser) {
        SysRolePermission update = new SysRolePermission();
        update.setId(rolePermissionId);
        update.setStatus(status);
        update.setOperationId(sysUser.getId());
        update.setUpdateTime(LocalDateTime.now());
        updateById(update);
    }
}
