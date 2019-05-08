package com.demo.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.service.BaseServiceImpl;
import com.demo.api.commons.status.SysStatus;
import com.demo.api.entity.SysOffice;
import com.demo.api.entity.SysUser;
import com.demo.api.mapper.SysOfficeMapper;
import com.demo.api.service.ISysOfficeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统部门表 服务实现类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Service
public class SysOfficeServiceImpl extends BaseServiceImpl<SysOfficeMapper, SysOffice> implements ISysOfficeService {

    @Resource
    private SysOfficeMapper sysOfficeMapper;

    public Page<SysOffice> findSysOfficePage(Page<SysOffice> sysOfficePage,SysOffice sysOffice) {
        return sysOfficeMapper.findSysOfficePage(sysOfficePage,sysOffice);
    }

    public void updateOrSave(SysOffice sysOffice, SysUser sysUser) {
        Long id = sysOffice.getId();
        if (id == null) {
            sysOffice.setCreateTime(LocalDateTime.now());
            sysOffice.setUpdateTime(LocalDateTime.now());
            sysOffice.setOperationId(sysUser.getId());
            sysOffice.setStatus(SysStatus.NORMAL.getValue());
            save(sysOffice);
        } else {
            sysOffice.setUpdateTime(LocalDateTime.now());
            sysOffice.setOperationId(sysUser.getId());
            updateById(sysOffice);
        }
    }

    public void updateStatus(Long id, Integer status,SysUser sysUser) {
        SysOffice update = new SysOffice();
        update.setId(id);
        update.setStatus(status);
        update.setUpdateTime(LocalDateTime.now());
        update.setOperationId(sysUser.getId());
        updateById(update);
    }
}
