package io.cms.core.configure.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.google.gson.Gson;

/**
 * 匿名用户访问资源失败处理
 */
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private Gson gson;

	public DefaultAuthenticationEntryPoint(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setHeader("Content-Type", "application/json;charset=utf-8");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		String responseJson = gson.toJson(new ResponseEntity<>(authException.getMessage(), HttpStatus.FORBIDDEN));
		response.getWriter().print(responseJson);
		response.getWriter().flush();
	}

}
