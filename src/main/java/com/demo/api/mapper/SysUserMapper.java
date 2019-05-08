package com.demo.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.entity.SysUser;
import com.demo.api.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    Page<SysUser> findSysUserPage(Page<SysUser> sysUserPage,@Param("sysUser") SysUser sysUser);

    List<SysUserRole> userRolesByUser(@Param("sysUserId") Long sysUerId);
}
