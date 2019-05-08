package com.demo.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.service.BaseServiceImpl;
import com.demo.api.entity.SysPermission;
import com.demo.api.entity.SysUser;
import com.demo.api.mapper.SysPermissionMapper;
import com.demo.api.service.ISysPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统权限表 服务实现类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Service
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    public Page<SysPermission> findPermissionPage(Page<SysPermission> sysPermissionPage,SysPermission sysPermission) {
        Page<SysPermission> page = sysPermissionMapper.findPermissionPage(sysPermissionPage,sysPermission);
        return page;
    }

    public void updateStatus(Long id, Integer status, SysUser sysUser) {
        SysPermission update = new SysPermission();
        update.setId(id);
        update.setStatus(status);
        update.setUpdateTime(LocalDateTime.now());
        update.setOperationId(sysUser.getId());
        updateById(update);
    }
}
