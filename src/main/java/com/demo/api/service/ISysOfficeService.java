package com.demo.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.service.IBaseService;
import com.demo.api.entity.SysOffice;
import com.demo.api.entity.SysUser;

/**
 * <p>
 * 系统部门表 服务类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface ISysOfficeService extends IBaseService<SysOffice> {

    Page<SysOffice> findSysOfficePage(Page<SysOffice> sysOfficePage,SysOffice sysOffice);

    void updateOrSave(SysOffice sysOffice, SysUser sysUser);

    void updateStatus(Long id, Integer status,SysUser sysUser);

}
