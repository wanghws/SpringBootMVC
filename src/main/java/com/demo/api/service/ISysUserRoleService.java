package com.demo.api.service;

import com.demo.api.commons.service.IBaseService;
import com.demo.api.entity.SysUser;
import com.demo.api.entity.SysUserRole;

import java.util.List;

/**
 * <p>
 * 系统用户角色表 服务类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface ISysUserRoleService extends IBaseService<SysUserRole> {

    void updateStatus(Long userRoleId, Integer status, SysUser sysUser);

    void createBatch(SysUser roleUser,List<SysUserRole> roleList);
}
