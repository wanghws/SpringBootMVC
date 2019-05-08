package com.demo.api.controller;


import com.demo.api.commons.controller.BaseController;
import com.demo.api.commons.entity.Response;
import com.demo.api.commons.utils.WebUtils;
import com.demo.api.entity.User;
import com.demo.api.service.IUserService;
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
 * 用户表 前端控制器
 * </p>
 */
@Api(tags = "用户相关接口")
@Slf4j
@Validated
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

	@Resource
	private IUserService userService;

	@ApiOperation(value = "注册账号")
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public Response register(
			@ApiParam(value = "邮箱", required = true)
			@NotNull(message = "邮箱不能为空")
			@RequestParam
					String email,
			@ApiParam(value = "密码", required = true)
			@NotNull(message = "密码不能为空")
			@Pattern(regexp = "[0-9]", message = "只允许数字")
			@RequestParam
					String password,
			@ApiIgnore
					HttpServletRequest request){

		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setRegisterIp(WebUtils.ipToInt(request));
		userService.register(user);
		String token = userService.generateToken(user);
		return new Response(token);
	}

	@ApiOperation(value = "登录")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public Response login(
			@ApiParam(value = "邮箱", required = true)
			@NotNull(message = "邮箱不能为空")
			@RequestParam
					String email,
			@ApiParam(value = "密码", required = true)
			@NotNull(message = "密码不能为空")
			@Pattern(regexp = "[0-9]", message = "只允许数字")
			@RequestParam
					String password,
			@ApiIgnore
					HttpServletRequest request) {

		User user = userService.lambdaQuery().eq(User::getEmail, email).one();
		if (null == user) {
			return new Response<>("账号不存在");
		}

		boolean result = userService.matchPassword(user, password);
		if (!result) {
			return new Response<>("密码错误");
		}

		user.setLoginIp(WebUtils.ipToInt(request));
		userService.login(user);

		String token = userService.generateToken(user);

		return new Response(token);
	}

	@ApiOperation(value = "退出登录")
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public Response logout(
			@ApiIgnore
					HttpServletRequest request) {
		String token = request.getHeader("x-authorization");
		userService.logout(token);
		return new Response();
	}

}

