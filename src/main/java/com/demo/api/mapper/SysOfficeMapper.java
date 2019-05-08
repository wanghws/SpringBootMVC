package com.demo.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.entity.SysOffice;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统部门表 Mapper 接口
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
public interface SysOfficeMapper extends BaseMapper<SysOffice> {

    Page<SysOffice> findSysOfficePage(Page<SysOffice> page,
                                      @Param("sysOffice") SysOffice sysOffice);
}
