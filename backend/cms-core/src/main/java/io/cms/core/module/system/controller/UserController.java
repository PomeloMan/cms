package io.cms.core.module.system.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.cms.core.configure.annotation.CurrentUser;
import io.cms.core.configure.annotation.LogOperation;
import io.cms.core.configure.page.IPage;
import io.cms.core.module.system.persistence.entity.User;
import io.cms.core.module.system.service.interfaces.IUserService;
import io.cms.core.module.system.vo.IUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "UserController")
public class UserController {

	@Autowired
	IUserService userService;

	/**
	 * @param principal
	 * @param user      {@link CurrentUserHandlerMethodArgumentResolver.class}
	 *                  自定义注解方法参数，返回处理后的数据
	 * @return
	 */
	@GetMapping("/me")
	@ApiOperation(value = "me")
	public Principal principal(@ApiIgnore Principal principal) {
		return principal;
	}

	/**
	 * @param iuser
	 * @return
	 */
	@PostMapping("list")
	@PreAuthorize("hasAnyAuthority('USER','USER_LIST')")
	@ApiOperation(value = "list")
	public ResponseEntity<Collection<User>> list(@RequestBody IUser view) {
		return new ResponseEntity<Collection<User>>(userService.query(view), HttpStatus.OK);
	}

	/**
	 * @param pageView
	 * @param user     {@link CurrentUserHandlerMethodArgumentResolver.class}
	 *                 自定义注解方法参数，返回处理后的数据
	 * @return
	 * @PreAuthorize.hasPermission {@link io.cms.core.configure.security.authentication.DefaultPermissionEvaluator.java}
	 */
	@PostMapping("page")
	@PreAuthorize("authenticated and hasPermission(#user, 'USER,USER_PAGE')") // DefaultPermissionEvaluator
	@ApiOperation(value = "page")
	public ResponseEntity<Page<IUser>> page(@RequestBody(required = true) IPage<IUser> pageView,
			@ApiIgnore @P("user") @CurrentUser Principal principal) {
		return new ResponseEntity<Page<IUser>>(userService.query(pageView, null), HttpStatus.OK);
	}

	@PostMapping("/save")
	@LogOperation("save")
	@PreAuthorize("hasAnyAuthority('USER','USER_SAVE')")
	@ApiOperation(value = "save")
	public ResponseEntity<User> save(@RequestBody IUser view) {
		return new ResponseEntity<User>(userService.saveOne(view), HttpStatus.OK);
	}

	@PostMapping("/delete")
	@LogOperation("delete")
	@PreAuthorize("hasAnyAuthority('USER','USER_DELETE')")
	@ApiOperation(value = "delete")
	public ResponseEntity<Object> logicDelete(@RequestBody Collection<String> ids) {
		userService.logicDelete(ids);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/physicalDelete")
	@LogOperation("physicalDelete")
	@PreAuthorize("hasAnyAuthority('USER')")
	@ApiOperation(value = "physicalDelete")
	public ResponseEntity<Object> physicalDelete(@RequestBody Collection<String> ids) {
		userService.physicalDelete(ids);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
