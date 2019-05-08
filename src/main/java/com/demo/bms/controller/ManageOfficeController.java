package com.demo.bms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.controller.BaseController;
import com.demo.api.commons.entity.Response;
import com.demo.api.commons.status.SysStatus;
import com.demo.api.commons.utils.DateUtil;
import com.demo.api.entity.SysOffice;
import com.demo.api.entity.SysUser;
import com.demo.api.service.ISysOfficeService;
import com.demo.api.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统部门表 前端控制器
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Api(tags = "系统部门相关接口")
@Slf4j
@Validated
@RestController
@RequestMapping("manage/office")
public class ManageOfficeController extends BaseController {

    @Resource
    private ISysOfficeService sysOfficeService;

    @Resource
    private ISysUserService sysUserService;

    @Secured("ROLE_OFFICE_LIST")
    @ApiOperation(value = "部门列表")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public Response<Page<SysOffice>> list(
            @ApiParam(name = "operationName",value = "操作人名字")
            @RequestParam(value = "operationName",required = false)
                    String operationName,
            @ApiParam(name = "parentName",value = "父部门名字")
            @RequestParam(value = "parentName",required = false)
                    String parentName,
            @ApiParam(name = "officeName",value = "部门名字")
            @RequestParam(value = "officeName",required = false)
                    String officeName,
            @ApiParam(name = "status",value = "状态")
            @RequestParam(value = "status",required = false)
                    Integer status,
            @ApiParam(name = "startTime",value = "创建时间 开始")
            @RequestParam(value = "startTime",required = false)
                    String startTime,
            @ApiParam(name = "endTime",value = "创建时间 结束")
            @RequestParam(value = "endTime",required = false)
                    String endTime,
            @ApiParam(name = "current", value = "页码",defaultValue = "1")
            @RequestParam(value = "current",required = false,defaultValue = "1")
                    Integer current,
            @ApiParam(name = "size", value = "分页数量",defaultValue = "10")
            @RequestParam(value = "size",required = false,defaultValue = "10")
                    Integer size){
        SysOffice querySysOffice = new SysOffice();
        querySysOffice.setOperationName(operationName);
        querySysOffice.setParentName(parentName);
        querySysOffice.setName(officeName);
        querySysOffice.setStatus(status);
        if (StringUtils.isNotBlank(startTime)){
            querySysOffice.setStartDate(DateUtil.timeParse2(startTime));
        }
        if (StringUtils.isNotBlank(endTime)){
            querySysOffice.setEndDate(DateUtil.timeParse2(endTime));
        }
        Page<SysOffice> page = sysOfficeService.findSysOfficePage(new Page<>(current, size),querySysOffice);
        return new Response<>(page);
    }

    @Secured("ROLE_OFFICE_SAVE")
    @ApiOperation(value = "创建/修改部门信息")
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public Response<SysOffice> save(
            @ApiParam(name = "parentId",value = "父部门ID")
            @RequestParam(value = "parentId",required = false)
                    Long parentId,
            @ApiParam(name = "id",value = "部门ID(修改时传入)")
            @RequestParam(value = "id",required = false)
                    Long id,
            @ApiParam(name = "name",value = "部门名字")
            @RequestParam(value = "name",required = false)
                    String name,
            @ApiParam(name = "remark",value = "部门备注")
            @RequestParam(value = "remark",required = false)
                    String remark, @ApiIgnore SysUser sysUser){

        if (parentId == null && StringUtils.isBlank(name) && StringUtils.isBlank(remark)) {
            return new Response<>();
        }
        SysOffice sysOffice;
        if (id != null){
            sysOffice = sysOfficeService.getById(id);
            if (sysOffice == null || sysOffice.getStatus() != SysStatus.NORMAL.getValue()) {
                return new Response<>(GlobalResult.SYS_OFFICE_NOT_FOUND);
            }
            if (StringUtils.isNotBlank(name)) {
                sysOffice.setName(name);
            }
            if (StringUtils.isNotBlank(remark)) {
                sysOffice.setRemark(remark);
            }
            if (parentId != null) {
                sysOffice.setParentId(parentId);
            }
        } else {
            if (StringUtils.isBlank(name)) {
                return new Response<>(GlobalResult.SYS_NAME_NOT_NULL);
            }
            sysOffice = new SysOffice();
            sysOffice.setParentId(parentId);
            sysOffice.setName(name);
            sysOffice.setRemark(remark);
        }
        sysOfficeService.updateOrSave(sysOffice,sysUser);
        Long officeParentId = sysOffice.getParentId();
        if (officeParentId != null){
            SysOffice parent = sysOfficeService.getById(officeParentId);
            sysOffice.setParentName(parent != null ? parent.getName() : "");
        }
        Long operationId = sysOffice.getOperationId();
        if (operationId != null && operationId != 0){
            SysUser operation = sysUserService.getById(operationId);
            sysOffice.setOperationName(operation != null ? operation.getLoginName() : "");
        }
        return new Response<>(sysOffice);
    }

    @Secured("ROLE_OFFICE_STATUS")
    @ApiOperation(value = "部门状态修改")
    @RequestMapping(value = "status",method = RequestMethod.POST)
    public Response status(
            @ApiParam(name = "id",value = "部门ID")
            @NotNull(message = GlobalResult.SYS_OFFICE_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id,
            @ApiParam(name = "status",value = "状态1：正常 2：删除")
            @NotNull(message = GlobalResult.SYS_STATUS_NOT_NULL)
            @RequestParam(value = "status")
                    Integer status,@ApiIgnore SysUser sysUser){
        SysOffice sysOffice = sysOfficeService.getById(id);
        if (sysOffice == null) {
            return new Response(GlobalResult.SYS_OFFICE_NOT_FOUND);
        }
        if (status.equals(sysOffice.getStatus())) {
            return new Response(GlobalResult.SYS_STATUS_INVALID);
        }
        sysOfficeService.updateStatus(id,status,sysUser);
        return new Response();
    }

    @Secured("ROLE_OFFICE_ALL")
    @ApiOperation(value = "查询全部部门")
    @RequestMapping(value = "all",method = RequestMethod.POST)
    public Response<List<SysOffice>> all(){
        List<SysOffice> list = sysOfficeService.lambdaQuery().eq(SysOffice::getStatus, SysStatus.NORMAL.getValue()).list();
        return new Response<>(list);
    }

    @ApiOperation(value = "部门树形菜单")
    @RequestMapping(value = "tree",method = RequestMethod.POST)
    public Response<List<Map<String,Object>>> tree(Long parentId){
        List<Map<String,Object>> officeList = new ArrayList<>();
        Map<String,Object> row;
        Map<String,Object> childrenMap;
        List<SysOffice> list;
        List<Map<String,Object>> childrenList;
        List<SysOffice> root =  sysOfficeService.lambdaQuery()
                .isNull(SysOffice::getParentId)
                .eq(SysOffice::getStatus, SysStatus.NORMAL.getValue())
                .list();

        for(SysOffice rootOffice:root){
            row = new HashMap<>();
            row.put("id",rootOffice.getId()+"");
            row.put("label",rootOffice.getName());

            list = sysOfficeService.lambdaQuery()
                    .eq(SysOffice::getParentId,rootOffice.getId())
                    .eq(SysOffice::getStatus, SysStatus.NORMAL.getValue())
                    .list();

            childrenList = new ArrayList<>();
            for(SysOffice office:list){
                childrenMap = new HashMap<>();
                childrenMap.put("id",office.getId()+"");
                childrenMap.put("label",office.getName());
                childrenList.add(childrenMap);
            }
            row.put("children",childrenList);
            officeList.add(row);
        }
        return new Response<>(officeList);
    }
}

