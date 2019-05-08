package com.demo.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.service.IBaseService;
import com.demo.api.entity.SysUser;
import com.demo.api.entity.SysUserRole;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface ISysUserService extends IBaseService<SysUser> {
    SysUser getUserByLoginName(String loginName);
    String login(String loginName,String password,int ip);
    void logout(String loginName);
    void save(SysUser user,SysUser manager);

    Page<SysUser> findSysUserPage(Page<SysUser> sysUserPage,SysUser sysUser);
    void updateUser(SysUser update, String loginName);
    void updateStatus(Long sysUserId, Integer status,String loginName,SysUser sysUser);
    List<SysUserRole> userRoleByUser(Long sysUerId);
    void evictUserCache(String loginName);
}
