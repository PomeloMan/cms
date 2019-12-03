package io.cms.core.module.system.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
@Api(value = "/", tags = "LoginController")
public class DefaultController {

	/**
	 * 此方法只为提供接口文档，没有实际意义。JWT 会直接拦截 '/login' 路径
	 */
	@PostMapping("/login")
	@ApiOperation(value = "用戶登陸接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query", example = "administrator"),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", example = "administrator") })
	public void user() {
	}
}
