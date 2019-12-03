package io.cms.core.module.system.controller;

import java.security.Principal;

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
import io.cms.core.configure.page.IPage;
import io.cms.core.module.system.service.interfaces.IRoleService;
import io.cms.core.module.system.vo.IRole;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	IRoleService roleService;

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
	public ResponseEntity<Page<IRole>> page(@RequestBody(required = true) IPage<IRole> pageView,
			@ApiIgnore @P("user") @CurrentUser Principal principal) {
		return new ResponseEntity<Page<IRole>>(roleService.query(pageView, null), HttpStatus.OK);
	}
}
