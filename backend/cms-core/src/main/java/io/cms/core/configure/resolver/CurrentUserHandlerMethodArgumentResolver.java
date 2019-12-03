package io.cms.core.configure.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.cms.core.configure.annotation.CurrentUser;
import io.cms.core.module.system.persistence.entity.User;

/**
 * 自定义参数注释处理类
 * @reference io.cms.core.module.system.controller.UserController.java page()
 */
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	public CurrentUserHandlerMethodArgumentResolver() {
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 如果参数类型是 User类或其子类，且注释是 @CurrentUser则进入 resolveArgument()方法
		return parameter.getParameterType().isAssignableFrom(User.class)
				&& parameter.hasParameterAnnotation(CurrentUser.class);
	}

	/**
	 * 处理参数并返回处理后的值
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
			WebDataBinderFactory factory) throws Exception {
		// handle parameter
		return parameter;
	}
}
