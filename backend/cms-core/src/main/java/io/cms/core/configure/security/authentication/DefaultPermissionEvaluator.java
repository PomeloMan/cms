package io.cms.core.configure.security.authentication;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * {@link https://docs.spring.io/spring-security/site/docs/5.0.0.RELEASE/reference/htmlsingle/#el-permission-evaluator}
 * 
 * @author PomeloMan
 */
@Component
public class DefaultPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		List<String> perms = Arrays.asList(permission.toString().toUpperCase().split(","));
		return authentication.getAuthorities().stream().filter(a -> perms.contains(a.getAuthority().toUpperCase()))
				.findAny().isPresent();
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// ...
		return false;
	}

}
