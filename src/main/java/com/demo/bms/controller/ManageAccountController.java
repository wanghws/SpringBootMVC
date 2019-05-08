package com.demo.bms.controller;


import com.demo.api.commons.constants.GlobalResult;
import com.demo.api.commons.controller.BaseController;
import com.demo.api.commons.entity.Response;
import com.demo.api.commons.security.JwtTokenProvider;
import com.demo.api.commons.utils.WebUtils;
import com.demo.api.entity.SysUser;
import com.demo.api.service.*;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Api(tags = "后台账号接口")
@Slf4j
@Validated
@RestController()
@RequestMapping("manage/account")
public class ManageAccountController extends BaseController {
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "登录")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Response login(
            @ApiParam(name = "loginname", value = "登录账号", required = true)
            @Pattern(regexp = "[a-z]", message = "只允许字符")
            @RequestParam
                    String loginname,
            @ApiParam(name = "password", value = "密码", required = true)
            @NotNull(message = "密码不能为空")
            @RequestParam
                    String password,
            @ApiIgnore
                    HttpServletRequest request
    ) {

        String token = sysUserService.login(loginname,password, WebUtils.ipToInt(request));
        if (Strings.isNullOrEmpty(token)){
            return new Response<>(GlobalResult.LOGIN_FAIL);
        }

        return new Response(token);
    }

    @ApiOperation(value = "用户信息")
    @RequestMapping(value = "info", method = RequestMethod.POST)
    public Response<SysUser> info(SysUser sysUser){
        return new Response<>(sysUser);
    }

    @ApiOperation(value = "登出")
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public Response logout(@ApiIgnore HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        String name = jwtTokenProvider.getUsername(token);
        sysUserService.logout(name);
        return new Response();
    }
}

