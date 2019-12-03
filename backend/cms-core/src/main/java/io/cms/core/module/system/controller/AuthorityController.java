package io.cms.core.module.system.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.cms.core.configure.annotation.CurrentUser;
import io.cms.core.configure.interceptor.PreCheck;
import io.cms.core.configure.page.IPage;
import io.cms.core.datasource.annotation.DataSource;
import io.cms.core.datasource.annotation.DataSource.DataSources;
import io.cms.core.module.system.persistence.entity.Authority;
import io.cms.core.module.system.service.interfaces.IAuthorityService;
import io.cms.core.module.system.vo.IAuthority;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/auth")
@Api(value = "/auth", tags = "AuthorityController")
public class AuthorityController {

	@Autowired
	IAuthorityService authService;

	/**
	 * @param pageView
	 * @param user     {@link CurrentUserHandlerMethodArgumentResolver.class}
	 *                 自定义注解方法参数，返回处理后的数据
	 * @return
	 * @PreAuthorize.hasPermission {@link io.cms.core.configure.security.authentication.DefaultPermissionEvaluator.java}
	 */
	@PostMapping("page")
	@PreAuthorize("authenticated and hasPermission(#user, 'ROLE,ROLE_PAGE')")
	@ApiOperation(value = "page")
	public ResponseEntity<Page<IAuthority>> page(@RequestBody(required = true) IPage<IAuthority> pageView,
			@ApiIgnore @P("user") @CurrentUser Principal principal) {
		return new ResponseEntity<Page<IAuthority>>(authService.query(pageView, null), HttpStatus.OK);
	}

	@PostMapping("query")
	@ApiOperation(value = "获取权限列表", notes = "头部需要带token信息")
	@DataSource(DataSources.SECONDARY)
	public ResponseEntity<Collection<Authority>> query(
			@RequestBody @ApiParam(name = "object", value = "传入json格式", required = true) IAuthority view) {
		return new ResponseEntity<Collection<Authority>>(authService.query(view), HttpStatus.OK);
	}

	@PreCheck
	@PostMapping("save")
	@DataSource(DataSources.PRIMARY)
	@ApiOperation(value = "获取权限列表", notes = "头部需要带token信息")
	public ResponseEntity<Authority> save(
			@ApiParam(name = "object", value = "传入json格式", required = true) IAuthority view) {
		return new ResponseEntity<Authority>(authService.saveOne(view), HttpStatus.OK);
	}
}
