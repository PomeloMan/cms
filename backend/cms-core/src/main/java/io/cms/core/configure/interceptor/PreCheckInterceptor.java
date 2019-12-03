package io.cms.core.configure.interceptor;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.cms.core.configure.exception.BusinessException;
import io.cms.util.security.IrreversibleEncryptor;

public class PreCheckInterceptor extends HandlerInterceptorAdapter {

	private final Log logger = LogFactory.getLog(PreCheckInterceptor.class);

	private Gson gson = new GsonBuilder().create();

	private final String FORM_TOKEN = "form_token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			PreCheck annotation = method.getAnnotation(PreCheck.class);
			if (annotation != null) {
				boolean repeatSubmitCheck = annotation.repeatSubmitCheck();
				if (repeatSubmitCheck) {
					repeatSubmitValidation(request);
					return true;
				}
				// ...
			}
			return true;
		} else {
			return super.preHandle(request, response, handler);
		}
	}

	/**
	 * 重复提交检查（url+参数相同视为重复提交）
	 * @param request
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws BusinessException
	 */
	private void repeatSubmitValidation(HttpServletRequest request)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, BusinessException {
		HttpSession session = request.getSession(true);

		String url = request.getRequestURI();
		String params = gson.toJson(request.getParameterMap());

		String present = IrreversibleEncryptor.encrypt(url + params, IrreversibleEncryptor.Algorithm.MD5);
		Object last = session.getAttribute(FORM_TOKEN);

		if (logger.isDebugEnabled()) {
			logger.debug("Present submit token -> " + present);
			logger.debug("Last submit token -> " + last);
		}

		if (last == null) {
			session.setAttribute(FORM_TOKEN, present);
		} else {
			if (StringUtils.equals(present, last.toString())) {
				throw new BusinessException("Request has been accepted"); // same submit
			} else {
				session.setAttribute(FORM_TOKEN, present);
			}
		}
	}
}
