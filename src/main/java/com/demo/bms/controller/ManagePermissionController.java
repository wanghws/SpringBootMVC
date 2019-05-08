package com.demo.bms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.controller.BaseController;
import com.demo.api.commons.entity.Response;
import com.demo.api.commons.status.SysStatus;
import com.demo.api.commons.utils.DateUtil;
import com.demo.api.entity.SysPermission;
import com.demo.api.entity.SysUser;
import com.demo.api.service.ISysPermissionService;
import com.demo.api.service.ISysUserService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统权限表 前端控制器
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Api(tags = "系统权限相关接口")
@Slf4j
@Validated
@RestController
@RequestMapping("manage/permission")
public class ManagePermissionController extends BaseController {

    @Resource
    private ISysPermissionService sysPermissionService;

    @Secured("ROLE_PERMISSION_LIST")
    @ApiOperation(value = "权限列表")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public Response<Page<SysPermission>> list(
            @ApiParam(name = "operationName",value = "操作人名字")
            @RequestParam(value = "operationName",required = false)
                    String operationName,
            @ApiParam(name = "parentName",value = "父权限名字")
            @RequestParam(value = "parentName",required = false)
                    String parentName,
            @ApiParam(name = "name",value = "权限名字")
            @RequestParam(value = "name",required = false)
                    String name,
            @ApiParam(name = "permission",value = "权限关键字")
            @RequestParam(value = "permission",required = false)
                    String permission,
            @ApiParam(name = "hidden",value = "是否显示0显示1隐藏")
            @RequestParam(value = "hidden",required = false)
                    Integer hidden,
            @ApiParam(name = "status",value = "状态 1正常 2删除")
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
                    Integer size){
        SysPermission querySysPermission = new SysPermission();
        querySysPermission.setOperationName(operationName);
        querySysPermission.setParentName(parentName);
        querySysPermission.setName(name);
        querySysPermission.setPermission(permission);
        querySysPermission.setHidden(hidden);
        querySysPermission.setStatus(status);
        if (StringUtils.isNotBlank(startTime)){
            querySysPermission.setStartDate(DateUtil.timeParse2(startTime));
        }
        if (StringUtils.isNotBlank(endTime)){
            querySysPermission.setEndDate(DateUtil.timeParse2(endTime));
        }
        Page<SysPermission> page = sysPermissionService.findPermissionPage(new Page<>(current, size),querySysPermission);
        return new Response<>(page);
    }

    @Secured("ROLE_PERMISSION_SAVE")
    @ApiOperation(value = "创建/修改权限信息")
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public Response<SysPermission> save(
            @ApiParam(name = "parentId",value = "父权限ID")
            @RequestParam(value = "parentId",required = false)
                    Long parentId,
            @ApiParam(name = "id",value = "权限记录ID修改时传入")
            @RequestParam(value = "id",required = false)
                    Long id,
            @ApiParam(name = "name",value = "权限名字")
            @RequestParam(value = "name",required = false)
                    String name,
            @ApiParam(name = "permission",value = "权限关键字")
            @RequestParam(value = "permission",required = false)
                    String permission,
            @ApiParam(name = "url",value = "url")
            @RequestParam(value = "url",required = false)
                    String url,
            @ApiParam(name = "remark",value = "备注")
            @RequestParam(value = "remark",required = false)
                    String remark,
            @ApiParam(name = "hidden",value = "是否显示 0显示 1隐藏")
            @RequestParam(value = "hidden",required = false)
                    Integer hidden,
            @ApiParam(name = "sort",value = "排序")
            @RequestParam(value = "sort",required = false)
                    Integer sort,
            @ApiIgnore SysUser sysUser){
        if (parentId == null
                && StringUtils.isBlank(name)
                && StringUtils.isBlank(permission)
                && StringUtils.isBlank(url)
                && StringUtils.isBlank(remark)
                && hidden == null
                && sort == null) {
            return new Response<>();
        }
        SysPermission sysPermission;
        if (StringUtils.isNotBlank(permission)){
            SysPermission one = sysPermissionService.lambdaQuery().eq(SysPermission::getStatus, SysStatus.NORMAL.getValue()).eq(SysPermission::getPermission, permission).one();
            if (one != null && id != null && !one.getId().equals(id)){
                return new Response<>(GlobalResult.SYS_PERMISSION_REPEAT);
            }
        }
        if (id != null) {
            sysPermission = sysPermissionService.getById(id);
            if (sysPermission == null || SysStatus.NORMAL.getValue() != sysPermission.getStatus()) {
                return new Response<>(GlobalResult.SYS_PERMISSION_RECORD_NOT_FOUND);
            }
            if (parentId != null && parentId != 0) {
                sysPermission.setParentId(parentId);
            }
            if (StringUtils.isNotBlank(name)) {
                sysPermission.setName(name);
            }
            if (StringUtils.isNotBlank(permission)) {
                sysPermission.setPermission(permission);
            }
            if (StringUtils.isNotBlank(remark)) {
                sysPermission.setRemark(remark);
            }
            if (hidden != null) {
                sysPermission.setHidden(hidden);
            }
            if (sort != null) {
                sysPermission.setSort(sort);
            }
            sysPermission.setUpdateTime(LocalDateTime.now());
            sysPermission.setOperationId(sysUser.getId());
            sysPermissionService.updateById(sysPermission);
        } else {
            sysPermission = new SysPermission();
            if (StringUtils.isBlank(name)) {
                return new Response(GlobalResult.SYS_NAME_NOT_NULL);
            }
            if (StringUtils.isBlank(permission)) {
                return new Response(GlobalResult.SYS_PERMISSION_NOT_NULL);
            }
            if (StringUtils.isBlank(url)) {
                return new Response(GlobalResult.SYS_URL_NOT_NULL);
            }
            sysPermission.setParentId(parentId);
            sysPermission.setName(name);
            sysPermission.setPermission(permission);
            sysPermission.setHidden(hidden);
            sysPermission.setStatus(SysStatus.NORMAL.getValue());
            sysPermission.setSort(sort);
            sysPermission.setRemark(remark);
            sysPermission.setCreateTime(LocalDateTime.now());
            sysPermission.setUpdateTime(LocalDateTime.now());
            sysPermission.setOperationId(sysUser.getId());
            sysPermissionService.save(sysPermission);
        }
        Long permissionParentId = sysPermission.getParentId();
        if (permissionParentId != null && permissionParentId != 0){
            SysPermission parent = sysPermissionService.getById(permissionParentId);
            sysPermission.setParentName(parent != null ? parent.getName() : "");
        }
        sysPermission.setOperationName(sysUser.getLoginName());
        return new Response<>(sysPermission);
    }

    @Secured("ROLE_PERMISSION_STATUS")
    @ApiOperation(value = "权限状态修改")
    @RequestMapping(value = "status",method = RequestMethod.POST)
    public Response status(
            @ApiParam(name = "id",value = "权限ID")
            @NotNull(message = GlobalResult.SYS_PERMISSION_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id,
            @ApiParam(name = "status",value = "状态 1:正常 2:删除")
            @Min(value = 1,message = GlobalResult.SYS_STATUS_INVALID)
            @Max(value = 2,message = GlobalResult.SYS_STATUS_INVALID)
            @NotNull(message = GlobalResult.SYS_STATUS_NOT_NULL)
            @RequestParam(value = "status")
                    Integer status,@ApiIgnore SysUser sysUser){
        SysPermission sysPermission = sysPermissionService.getById(id);
        if (sysPermission == null) {
            return new Response(GlobalResult.SYS_PERMISSION_RECORD_NOT_FOUND);
        }
        if (status.equals(sysPermission.getStatus())) {
            return new Response(GlobalResult.SYS_STATUS_INVALID);
        }
        sysPermissionService.updateStatus(id,status,sysUser);
        return new Response();
    }

    @Secured("ROLE_PERMISSION_ALL")
    @ApiOperation(value = "查询全部权限")
    @RequestMapping(value = "all",method = RequestMethod.POST)
    public Response<List<SysPermission>> all(){
        List<SysPermission> list = sysPermissionService.lambdaQuery().eq(SysPermission::getStatus, SysStatus.NORMAL.getValue()).list();
        return new Response<>(list);
    }

    @ApiOperation(value = "权限树形菜单")
    @RequestMapping(value = "tree",method = RequestMethod.POST)
    public Response<List<Map<String,Object>>> tree(Long parentId){
        List<Map<String,Object>> permissionList = new ArrayList<>();
        Map<String,Object> row;
        Map<String,Object> childrenMap;
        List<SysPermission> list;
        List<Map<String,Object>> childrenList;
        List<SysPermission> root =  sysPermissionService.lambdaQuery()
                .isNull(SysPermission::getParentId)
                .eq(SysPermission::getStatus, SysStatus.NORMAL.getValue())
                .list();

        for(SysPermission rootPermission:root){
            row = Maps.newHashMap();
            row.put("id",rootPermission.getId()+"");
            row.put("label",rootPermission.getName());

            list = sysPermissionService.lambdaQuery()
                    .eq(SysPermission::getParentId,rootPermission.getId())
                    .eq(SysPermission::getStatus, SysStatus.NORMAL.getValue())
                    .list();

            childrenList = new ArrayList<>();
            for(SysPermission office:list){
                childrenMap = new HashMap<>();
                childrenMap.put("id",office.getId()+"");
                childrenMap.put("label",office.getName());
                childrenList.add(childrenMap);
            }
            row.put("children",childrenList);
            permissionList.add(row);
        }
        return new Response<>(permissionList);
    }
}

