package com.demo.bms.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.controller.BaseController;
import com.demo.api.commons.entity.Response;
import com.demo.api.commons.status.SysStatus;
import com.demo.api.commons.utils.DateUtil;
import com.demo.api.entity.*;
import com.demo.api.service.ISysOfficeService;
import com.demo.api.service.ISysPermissionService;
import com.demo.api.service.ISysRolePermissionService;
import com.demo.api.service.ISysRoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Api(tags = "系统角色相关接口")
@Slf4j
@Validated
@RestController
@RequestMapping("manage/role")
public class ManageRoleController extends BaseController {

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysRolePermissionService sysRolePermissionService;

    @Resource
    private ISysOfficeService sysOfficeService;

    @Resource
    private ISysPermissionService sysPermissionService;

    @Secured("ROLE_LIST")
    @ApiOperation(value = "角色列表")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public Response<Page<SysRole>> list(
            @ApiParam(name = "operationName",value = "操作人名字")
            @RequestParam(value = "operationName",required = false)
                    String operationName,
            @ApiParam(name = "name",value = "角色名")
            @RequestParam(value = "name",required = false)
                    String name,
            @ApiParam(name = "officeName",value = "部门名")
            @RequestParam(value = "officeName",required = false)
                    String officeName,
            @ApiParam(name = "status",value = "状态 1正常，2删除")
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
        SysRole querySysRole = new SysRole();
        querySysRole.setOperationName(operationName);
        querySysRole.setName(name);
        querySysRole.setOfficeName(officeName);
        querySysRole.setStatus(status);
        if (StringUtils.isNotBlank(startTime)){
            querySysRole.setStartDate(DateUtil.timeParse2(startTime));
        }
        if (StringUtils.isNotBlank(endTime)){
            querySysRole.setEndDate(DateUtil.timeParse2(endTime));
        }
        Page<SysRole> page = sysRoleService.findRolePage(new Page<>(current,size),querySysRole);
        return new Response<>(page);
    }

    @Secured("ROLE_SAVE")
    @ApiOperation(value = "创建/修改角色信息")
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public Response<SysRole> save(
            @ApiParam(name = "id",value = "角色ID(修改时传入)")
            @RequestParam(value = "id",required = false)
                    Long id,
            @ApiParam(name = "name",value = "角色名")
            @RequestParam(value = "name",required = false)
                    String name ,
            @ApiParam(name = "officeId",value = "部门ID")
            @RequestParam(value = "officeId",required = false)
                    Long officeId,
            @ApiParam(name = "remark",value = "备注")
            @RequestParam(value = "remark",required = false)
                    String remark,
            @ApiIgnore SysUser sysUser){
        if (StringUtils.isBlank(name)&& officeId == null && remark == null){
            return new Response<>();
        }
        SysRole sysRole;
        if (id != null) {
            sysRole = sysRoleService.getById(id);
            if (sysRole == null) {
                return new Response<>(GlobalResult.SYS_ROLE_NOT_FOUND);
            }
            if (sysRole.getStatus() == SysStatus.DELETE.getValue()){
                return new Response<>(GlobalResult.SYS_STATUS_INVALID);
            }
            if (StringUtils.isNotBlank(name)){
                sysRole.setName(name);
            }
            if (officeId != null && officeId != 0) {
                sysRole.setOfficeId(officeId);
            }
            if (StringUtils.isNotBlank(remark)){
                sysRole.setRemark(remark);
            }
            SysRole update = new SysRole();
            update.setId(id);
            update.setName(name);
            update.setOfficeId(officeId);
            update.setRemark(remark);
            sysRoleService.updateRole(update,sysUser);
        } else {
            sysRole = new SysRole();
            if (StringUtils.isBlank(name)){
                return new Response<>(GlobalResult.SYS_NAME_NOT_NULL);
            }
            if (officeId == null){
                return new Response<>(GlobalResult.SYS_OFFICE_ID_NOT_NULL);
            }
            sysRole.setName(name);
            sysRole.setRemark(remark);
            sysRole.setOperationId(sysUser.getId());
            sysRole.setOfficeId(officeId);
            sysRole.setCreateTime(LocalDateTime.now());
            sysRole.setStatus(SysStatus.NORMAL.getValue());
            sysRole.setUpdateTime(LocalDateTime.now());
            sysRoleService.save(sysRole);
        }
        if (officeId != null && officeId != 0){
            SysOffice office = sysOfficeService.getById(officeId);
            sysRole.setOfficeName(office != null ? office.getName() : "");
        }
        sysRole.setOperationName(sysUser.getLoginName());
        return new Response<>(sysRole);
    }

    @Secured("ROLE_STATUS")
    @ApiOperation(value = "角色状态修改")
    @RequestMapping(value = "status",method = RequestMethod.POST)
    public Response status(
            @ApiParam(name = "id",value = "角色表ID")
            @NotNull(message = GlobalResult.SYS_ROLE_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id,
            @ApiParam(name = "status",value = "角色状态 1正常 2删除")
            @NotNull(message = GlobalResult.SYS_STATUS_NOT_NULL)
            @Min(value = 1,message = GlobalResult.SYS_STATUS_INVALID)
            @Max(value = 2,message = GlobalResult.SYS_STATUS_INVALID)
            @RequestParam(value = "status")
                    Integer status,
            @ApiIgnore SysUser sysUser){
        SysRole sysRole = sysRoleService.getById(id);
        if (sysRole == null) {
            return new Response(GlobalResult.SYS_ROLE_NOT_FOUND);
        }
        if (sysRole.getStatus().equals(status)) {
            return new Response(GlobalResult.SYS_STATUS_INVALID);
        }
        sysRoleService.updateStatus(id,status,sysUser);
        return new Response();
    }

    @Secured("ROLE_PERMISSION_SAVE")
    @ApiOperation(value = "角色权限关系保存")
    @RequestMapping(value = "permission/save",method = RequestMethod.POST)
    public Response permissionSave(
            @ApiParam(name = "id",value = "角色ID")
            @NotNull(message = GlobalResult.SYS_ROLE_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id,
            @ApiParam(name = "permissionIds",value = "权限ID逗号分隔")
            @NotNull(message = GlobalResult.SYS_PERMISSION_ID_NOT_NULL)
            @RequestParam(value = "permissionIds")
                    String[] permissionIds,
            @ApiIgnore SysUser sysUser){
        List<SysRolePermission> list = sysRolePermissionService.lambdaQuery().eq(SysRolePermission::getRoleId, id).list();
        List<SysRolePermission> oldPermissionList = Lists.newArrayList();
        list.forEach(sysRolePermission -> {
            sysRolePermission.setStatus(SysStatus.DELETE.getValue());
            oldPermissionList.add(sysRolePermission);
        });
        if (!CollectionUtils.isEmpty(oldPermissionList)){
            sysRolePermissionService.updateBatchById(oldPermissionList);
        }
        List<SysRolePermission> permissionList = Lists.newArrayList();
        for (int i = 0; i < permissionIds.length; i++) {
            Long permissionId;
            try {
                permissionId = Long.valueOf(permissionIds[i]);
            } catch (NumberFormatException e) {
                log.error("NumberFormat error:{}",e);
                return new Response(GlobalResult.SYS_PERMISSION_ID_NOT_NULL);
            }
            List<SysPermission> permissions = sysPermissionService.lambdaQuery().eq(SysPermission::getParentId, permissionId).list();
            if (!CollectionUtils.isEmpty(permissions)){
                permissions.forEach(sysPermission -> {
                    if (sysPermission.getId().equals(permissionId)) return;
                    SysRolePermission permission = new SysRolePermission();
                    permission.setRoleId(id);
                    permission.setPermissionId(sysPermission.getId());
                    permission.setOperationId(sysUser.getId());
                    permission.setStatus(SysStatus.NORMAL.getValue());
                    permission.setCreateTime(LocalDateTime.now());
                    permission.setUpdateTime(LocalDateTime.now());
                    permissionList.add(permission);
                });
            }
            SysRolePermission permission = new SysRolePermission();
            permission.setRoleId(id);
            permission.setPermissionId(permissionId);
            permission.setOperationId(sysUser.getId());
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            permission.setStatus(SysStatus.NORMAL.getValue());
            permissionList.add(permission);
        }
        sysRolePermissionService.saveBatch(permissionList);
        return new Response();
    }

    @Secured("ROLE_PERMISSION_LIST")
    @ApiOperation(value = "角色权限关系列表")
    @RequestMapping(value = "permission/list",method = RequestMethod.POST)
    public Response<Page<SysRolePermission>> permissionList(
            @ApiParam(name = "roleName",value = "角色名")
            @RequestParam(value = "roleName",required = false)
                    String roleName,
            @ApiParam(name = "permissionName",value = "权限名字")
            @RequestParam(value = "permissionName",required = false)
                    String permissionName,
            @ApiParam(name = "operationName",value = "操作人名字")
            @RequestParam(value = "operationName",required = false)
                    String operationName,
            @ApiParam(name = "status",value = "状态 0:删除 1:正常")
            @RequestParam(value = "status",required = false)
                    Integer status,
            @ApiParam(name = "startTime",value = "时间查询开始")
            @RequestParam(value = "startTime",required = false)
                    String startTime,
            @ApiParam(name = "endTime",value = "时间查询结束")
            @RequestParam(value = "endTime",required = false)
                    String endTime,
            @ApiParam(name = "current", value = "页码",defaultValue = "1")
            @RequestParam(value = "current",required = false,defaultValue = "1")
                    Integer current,
            @ApiParam(name = "size", value = "分页数量",defaultValue = "10")
            @RequestParam(value = "size",required = false,defaultValue = "10")
                    Integer size){
        SysRolePermission querySysRolePermission = new SysRolePermission();
        querySysRolePermission.setRoleName(roleName);
        querySysRolePermission.setPermissionName(permissionName);
        querySysRolePermission.setOperationName(operationName);
        querySysRolePermission.setStatus(status);
        if (StringUtils.isNotBlank(startTime)){
            querySysRolePermission.setStartDate(DateUtil.timeParse2(startTime));
        }
        if (StringUtils.isNotBlank(endTime)){
            querySysRolePermission.setEndDate(DateUtil.timeParse2(endTime));
        }
        Page<SysRolePermission> page = sysRoleService.findRolePermissionPage(new Page<>(current,size),querySysRolePermission);
        return new Response<>(page);
    }

    @Secured("ROLE_PERMISSIONS_ROLE")
    @ApiOperation(value = "角色对应所有权限")
    @RequestMapping(value = "permissions/role",method = RequestMethod.POST)
    public Response<List<SysRolePermission>> permissionsByRole(Long roleId){
        List<SysRolePermission> list = sysRoleService.findRolePermissionsByRole(roleId);
        return new Response<>(list);
    }

    @Secured("ROLE_PERMISSION_STATUS")
    @ApiOperation(value = "角色权限关系状态修改")
    @RequestMapping(value = "permission/status",method = RequestMethod.POST)
    public Response permissionStatus(
            @ApiParam(name = "rolePermissionId",value = "列表返回的主键ID")
            @NotNull(message = GlobalResult.SYS_ROLE_PERMISSION_ID_NOT_NULL)
            @RequestParam(value = "rolePermissionId")
                    Long rolePermissionId,
            @ApiParam(name = "status",value = "状态 0:删除，1:正常")
            @Min(value = 1,message = GlobalResult.SYS_STATUS_INVALID)
            @Max(value = 1,message = GlobalResult.SYS_STATUS_INVALID)
            @NotNull(message = GlobalResult.SYS_STATUS_NOT_NULL)
            @RequestParam(value = "status")
                    Integer status,
            @ApiIgnore SysUser sysUser){
        SysRolePermission sysRolePermission = sysRolePermissionService.getById(rolePermissionId);
        if (sysRolePermission == null) {
            return new Response(GlobalResult.SYS_ROLE_PERMISSION_NOT_FOUND);
        }
        if (sysRolePermission.getStatus().equals(status)){
            return new Response(GlobalResult.SYS_STATUS_INVALID);
        }
        sysRolePermissionService.updateStatus(rolePermissionId,status,sysUser);
        return new Response();
    }

    @Secured("ROLE_ALL")
    @ApiOperation(value = "查询全部角色")
    @RequestMapping(value = "all",method = RequestMethod.POST)
    public Response<List<Map<String,Object>>> all(){
        List<Map<String,Object>> roleList = Lists.newArrayList();
        Map<String,Object> row;
        List<SysRole> root =  sysRoleService.lambdaQuery().eq(SysRole::getStatus, SysStatus.NORMAL.getValue()).list();

        for(SysRole rootSysRole:root){
            row = Maps.newHashMap();
            row.put("id",rootSysRole.getId()+"");
            row.put("label",rootSysRole.getName());
            roleList.add(row);
        }
        return new Response<>(roleList);
    }

    @ApiOperation(value = "获取角色所有权限")
    @RequestMapping(value = "current/permissions",method = RequestMethod.POST)
    public Response<Set<String>> currentPermissions(
            @ApiParam(name = "id",value = "角色ID")
            @NotNull(message = GlobalResult.SYS_ROLE_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id){
        List<SysRolePermission> list = sysRolePermissionService.lambdaQuery().eq(SysRolePermission::getRoleId, id).eq(SysRolePermission::getStatus, SysStatus.NORMAL.getValue()).list();
        if (CollectionUtils.isEmpty(list)){
            return new Response<>(Sets.newHashSet());
        }
        Set<String> set = Sets.newHashSet();
        for (SysRolePermission permission : list) {
            set.add(permission.getPermissionId().toString());
        }
        return new Response<>(set);
    }
}

