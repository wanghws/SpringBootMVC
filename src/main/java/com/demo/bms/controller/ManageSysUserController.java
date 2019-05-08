package com.demo.bms.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.controller.BaseController;
import com.demo.api.commons.entity.Response;
import com.demo.api.commons.status.SysStatus;
import com.demo.api.commons.status.UserStatus;
import com.demo.api.commons.utils.DateUtil;
import com.demo.api.commons.utils.WebUtils;
import com.demo.api.entity.SysOffice;
import com.demo.api.entity.SysUser;
import com.demo.api.entity.SysUserRole;
import com.demo.api.service.ISysOfficeService;
import com.demo.api.service.ISysUserRoleService;
import com.demo.api.service.ISysUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 管理后台用户
 * </p>
 *
 * @author wanghw
 * @since 2019-03-08
 */
@Api(tags = "管理后台用户相关接口")
@Slf4j
@Validated
@RestController
@RequestMapping("manage/sys")
public class ManageSysUserController extends BaseController {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private ISysOfficeService sysOfficeService;

    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Secured("ROLE_SYS_LIST")
    @ApiOperation(value = "sys用户列表")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public Response<Page<SysUser>> sysList(
            @ApiParam(name = "officeName",value = "部门名")
            @RequestParam(value = "officeName",required = false)
                    String officeName,
            @ApiParam(name = "loginName",value = "用户名")
            @RequestParam(value = "loginName",required = false)
                    String loginName,
            @ApiParam(name = "mobile",value = "手机号")
            @RequestParam(value = "mobile",required = false)
                    String mobile,
            @ApiParam(name = "email",value = "邮箱")
            @RequestParam(value = "email",required = false)
                    String email,
            @ApiParam(name = "status",value = "状态")
            @RequestParam(value = "status",required = false)
                    Integer status,
            @ApiParam(name = "regStartTime",value = "注册开始时间")
            @RequestParam(value = "regStartTime",required = false)
                    String regStartTime,
            @ApiParam(name = "regEndTime",value = "注册结束时间")
            @RequestParam(value = "regEndTime",required = false)
                    String regEndTime,
            @ApiParam(name = "loginStartTime",value = "登录开始时间")
            @RequestParam(value = "loginStartTime",required = false)
                    String loginStartTime,
            @ApiParam(name = "loginEndTime",value = "登录结束时间")
            @RequestParam(value = "loginEndTime",required = false)
                    String loginEndTime,
            @ApiParam(name = "current", value = "页码",defaultValue = "1")
            @RequestParam(value = "current",required = false,defaultValue = "1")
                    Integer current,
            @ApiParam(name = "size", value = "分页数量",defaultValue = "10")
            @RequestParam(value = "size",required = false,defaultValue = "10")
                    Integer size){

        SysUser querySysUser = new SysUser();
        querySysUser.setOfficeName(officeName);
        querySysUser.setLoginName(loginName);
        querySysUser.setEmail(email);
        querySysUser.setStatus(status);
        if (StringUtils.isNotBlank(regStartTime)){
            querySysUser.setRegStartDate(DateUtil.timeParse2(regStartTime));
        }
        if (StringUtils.isNotBlank(regEndTime)){
            querySysUser.setRegEndDate(DateUtil.timeParse2(regEndTime));
        }
        if (StringUtils.isNotBlank(loginStartTime)){
            querySysUser.setLoginStartDate(DateUtil.timeParse2(loginStartTime));
        }
        if (StringUtils.isNotBlank(loginEndTime)){
            querySysUser.setLoginEndDate(DateUtil.timeParse2(loginEndTime));
        }
        Page<SysUser> page = sysUserService.findSysUserPage(new Page<>(current,size),querySysUser);
        return new Response<>(page);
    }

    @Secured("ROLE_SYS_CREATE")
    @ApiOperation(value = "创建系统用户")
    @RequestMapping(value = "create",method = RequestMethod.POST)
    public Response<SysUser> sysCreate(
            @ApiParam(name = "loginName",value = "登录名")
            @NotNull(message = GlobalResult.SYS_NAME_NOT_NULL)
            @RequestParam(value = "loginName")
                    String loginName,
            @ApiParam(name = "password",value = "密码")
            @NotNull(message = GlobalResult.SYS_PASSWORD_NOT_NULL)
            @RequestParam(value = "password")
                    String password,
            @ApiParam(name = "email",value = "邮箱")
            @RequestParam(value = "email",required = false)
                    String email,
            @ApiParam(name = "officeId",value = "部门ID")
            @NotNull(message = GlobalResult.SYS_OFFICE_ID_NOT_NULL)
            @RequestParam(value = "officeId")
                    Long officeId,HttpServletRequest request){
        SysUser userByName = sysUserService.lambdaQuery().eq(SysUser::getLoginName, loginName).one();
        if (userByName != null) {
            return new Response<>(GlobalResult.SYS_NAME_NOT_REPEAT);
        }

        if (StringUtils.isNotBlank(email)){
            if (!WebUtils.checkPatten(email,"[a-z]")){
                return new Response<>("邮箱格式错误");
            }
            SysUser userByEmail = sysUserService.lambdaQuery().eq(SysUser::getEmail, email).one();
            if (userByEmail != null){
                return new Response<>(GlobalResult.SYS_EMAIL_NOT_REPEAT);
            }
        }
        SysOffice sysOffice = sysOfficeService.getById(officeId);
        if (sysOffice == null){
            return new Response<>(GlobalResult.SYS_OFFICE_NOT_FOUND);
        }
        if (SysStatus.DELETE.getValue() == sysOffice.getStatus()){
            return new Response<>(GlobalResult.SYS_OFFICE_STATUS_INVALID);
        }
        SysUser sysUser = new SysUser();
        sysUser.setLoginName(loginName);
        sysUser.setPassword(passwordEncoder.encode(password));
        sysUser.setEmail(email);
        sysUser.setOfficeId(officeId);
        sysUser.setRegisterIp(WebUtils.ipToInt(request));
        sysUser.setRegisterTime(LocalDateTime.now());
        sysUser.setStatus(UserStatus.NORMAL.getValue());
        sysUser.setOfficeName(sysOffice.getName());
        sysUserService.save(sysUser);
        return new Response<>(sysUser);
    }

    @Secured("ROLE_SYS_UPDATE")
    @ApiOperation(value = "系统用户修改")
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Response<SysUser> sysUpdate(
            @ApiParam(name = "id",value = "系统用户ID")
            @NotNull(message = GlobalResult.SYS_USER_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id,
            @ApiParam(name = "mobile",value = "手机号")
            @RequestParam(value = "mobile",required = false)
                    String mobile,
            @ApiParam(name = "email",value = "邮箱")
            @RequestParam(value = "email",required = false)
                    String email,
            @ApiParam(name = "officeId",value = "部门ID")
            @RequestParam(value = "officeId",required = false)
                    Long officeId,@ApiIgnore HttpServletRequest request){
        if (StringUtils.isBlank(mobile)
                && StringUtils.isBlank(email)
                && officeId == null){
            return new Response<>();
        }
        SysUser user = sysUserService.getById(id);
        if (user == null || user.getStatus() != SysStatus.NORMAL.getValue()) {
            return new Response<>(GlobalResult.SYS_USER_NOT_FOUND);
        }
        SysUser update = new SysUser();
        if (StringUtils.isNotBlank(email)){
            if (!WebUtils.checkPatten(email,"a-z")){
                return new Response<>("邮箱格式错误");
            }
            SysUser userByEmail = sysUserService.lambdaQuery().eq(SysUser::getEmail, email).one();
            if (userByEmail != null && !email.equals(userByEmail.getEmail())){
                return new Response<>(GlobalResult.SYS_EMAIL_NOT_REPEAT);
            }
            update.setEmail(email);
            user.setEmail(email);
        }
        if (officeId != null) {
            SysOffice sysOffice = sysOfficeService.getById(officeId);
            if (sysOffice == null || SysStatus.NORMAL.getValue() != sysOffice.getStatus()){
                return new Response<>(GlobalResult.SYS_OFFICE_NOT_FOUND);
            }
            update.setOfficeId(officeId);
            user.setOfficeId(officeId);
            user.setOfficeName(sysOffice.getName());
        }
        update.setId(id);
        sysUserService.updateById(update);
        sysUserService.evictUserCache(user.getLoginName());
        return new Response<>(user);
    }

    @Secured("ROLE_SYS_STATUS")
    @ApiOperation(value = "修改系统用户状态")
    @RequestMapping(value = "status",method = RequestMethod.POST)
    public Response sysStatus(
            @ApiParam(name = "id",value = "系统用户ID")
            @NotNull(message = GlobalResult.SYS_USER_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id,
            @ApiParam(name = "status",value = "状态 0:禁用 1:正常 2:锁定")
            @NotNull(message = GlobalResult.SYS_STATUS_NOT_NULL)
            @Min(value = 0,message = GlobalResult.SYS_STATUS_INVALID)
            @Max(value = 2,message = GlobalResult.SYS_STATUS_INVALID)
            @RequestParam(value = "status")
                    Integer status,@ApiIgnore SysUser sysUser){
        SysUser user = sysUserService.getById(id);
        if (user == null){
            return new Response(GlobalResult.SYS_USER_NOT_FOUND);
        }
        if (user.getStatus().equals(status)){
            return new Response(GlobalResult.SYS_STATUS_INVALID);
        }
        SysUser update= new SysUser();
        update.setStatus(status);
        update.setId(user.getId());
        sysUserService.updateById(update);
        sysUserService.evictUserCache(user.getLoginName());
        return new Response();
    }

    @Secured("ROLE_SYS_ROLE_SAVE")
    @ApiOperation(value = "后台用户角色关系保存")
    @RequestMapping(value = "role/save", method = RequestMethod.POST)
    public Response saveRole(
            @ApiParam(name = "id", value = "用户ID")
            @NotNull(message = GlobalResult.SYS_USER_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id,
            @ApiParam(name = "roleIds", value = "角色ID，逗号分隔")
            @NotNull(message = GlobalResult.SYS_ROLE_ID_NOT_NULL)
            @RequestParam(value = "roleIds")
                    String[] roleIds,
            @ApiIgnore SysUser sysUser) {
        List<SysUserRole> list = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, id).eq(SysUserRole::getStatus, SysStatus.NORMAL.getValue()).list();
        List<SysUserRole> oldUserRoles = Lists.newArrayList();
        list.forEach(sysUserRole -> {
            sysUserRole.setStatus(SysStatus.DELETE.getValue());
            oldUserRoles.add(sysUserRole);
        });
        if (!CollectionUtils.isEmpty(oldUserRoles)){
            sysUserRoleService.updateBatchById(oldUserRoles);
        }
        SysUser userById = sysUserService.getById(id);
        if (userById == null) {
            return new Response(GlobalResult.SYS_USER_NOT_FOUND);
        }
        if (userById.getStatus() != UserStatus.NORMAL.getValue()) {
            return new Response(GlobalResult.SYS_USER_STATUS_INVALID);
        }
        List<SysUserRole> roleList = Lists.newArrayList();
        for (int i = 0; i < roleIds.length; i++) {
            Long roleId;
            try {
                roleId = Long.valueOf(roleIds[i]);
            } catch (NumberFormatException e) {
                log.error("error :{}", e);
                return new Response(GlobalResult.SYS_ROLE_ID_NOT_NULL);
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(id);
            sysUserRole.setStatus(SysStatus.NORMAL.getValue());
            sysUserRole.setCreateTime(LocalDateTime.now());
            sysUserRole.setOperationId(sysUser.getId());
            sysUserRole.setUpdateTime(LocalDateTime.now());
            roleList.add(sysUserRole);
        }
        SysUser roleUser = sysUserService.getById(id);
        sysUserRoleService.createBatch(roleUser,roleList);
        return new Response();
    }

    @Secured("ROLE_SYS_ROLES")
    @ApiOperation(value = "后台用户对应所有角色")
    @RequestMapping(value = "roles",method = RequestMethod.POST)
    public Response<List<SysUserRole>> userRoleByUser(
            @ApiParam(name = "sysUerId", value = "用户ID")
            @NotNull(message = GlobalResult.SYS_USER_ID_NOT_NULL)
            @RequestParam(value = "sysUerId")
                    Long sysUerId){
        SysUser userById = sysUserService.getById(sysUerId);
        if (userById == null){
            return new Response(GlobalResult.SYS_USER_NOT_FOUND);
        }
        if (userById.getStatus() != UserStatus.NORMAL.getValue()){
            return new Response(GlobalResult.SYS_USER_STATUS_INVALID);
        }
        List<SysUserRole> list = sysUserService.userRoleByUser(sysUerId);
        return new Response<>(list);
    }

    @Secured("ROLE_SYS_ROLE_STATUS")
    @ApiOperation(value = "后台用户角色关系状态修改")
    @RequestMapping(value = "role/status",method = RequestMethod.POST)
    public Response roleStatus(
            @ApiParam(name = "userRoleId", value = "用户角色关系ID")
            @NotNull(message = GlobalResult.SYS_USER_ROLE_ID_NOT_NULL)
            @RequestParam(value = "userRoleId")
                    Long userRoleId,
            @ApiParam(name = "status", value = "状态 1正常 2禁用")
            @NotNull(message = GlobalResult.SYS_STATUS_NOT_NULL)
            @RequestParam(value = "status")
                    Integer status,
            @ApiIgnore SysUser sysUser){
        SysUserRole userRoleById = sysUserRoleService.getById(userRoleId);
        if (userRoleById == null){
            return new Response(GlobalResult.SYS_USER_ROLE_NOT_FOUND);
        }
        if (userRoleById.getStatus().equals(status)){
            return new Response(GlobalResult.SYS_STATUS_INVALID);
        }
        sysUserRoleService.updateStatus(userRoleId,status,sysUser);
        return new Response();
    }

    @ApiOperation(value = "获取当前用户角色ID集合")
    @RequestMapping(value = "current/roles",method = RequestMethod.POST)
    public Response<Set<String>> myRoles(
            @ApiParam(name = "id", value = "用户ID")
            @NotNull(message = GlobalResult.SYS_USER_ID_NOT_NULL)
            @RequestParam(value = "id")
                    Long id){
        List<SysUserRole> list = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, id).eq(SysUserRole::getStatus, SysStatus.NORMAL.getValue()).list();
        if (CollectionUtils.isEmpty(list)){
            return new Response<>(Sets.newHashSet());
        }
        Set<String> set = Sets.newHashSet();
        for (SysUserRole userRole : list) {
            set.add(userRole.getRoleId().toString());
        }
        return new Response<>(set);
    }
}

