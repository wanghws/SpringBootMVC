package com.demo.api.service.impl;

import com.demo.api.commons.service.BaseServiceImpl;
import com.demo.api.entity.SysUser;
import com.demo.api.entity.SysUserRole;
import com.demo.api.mapper.SysUserRoleMapper;
import com.demo.api.service.ISysUserRoleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统用户角色表 服务实现类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    public void updateStatus(Long userRoleId, Integer status, SysUser sysUser) {
        SysUserRole update = new SysUserRole();
        update.setId(userRoleId);
        update.setStatus(status);
        update.setUpdateTime(LocalDateTime.now());
        update.setOperationId(sysUser.getId());
        updateById(update);
    }

    @CacheEvict(value = "user_auth",key = "#roleUser.loginName")
    public void createBatch(SysUser roleUser, List<SysUserRole> roleList) {
        saveBatch(roleList);
    }
}
