package io.cms.core.configure.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.cms.core.configure.annotation.LogOperation;
import io.cms.core.configure.enums.Status;
import io.cms.core.module.log.persistence.entity.Log;
import io.cms.core.module.log.service.interfaces.ILogService;
import io.cms.util.HttpContextUtils;
import io.cms.util.http.IpUtils;

@Aspect
@Component
public class LogOperationAspect {

	@Autowired
	private Gson gson;
	@Autowired
	private ILogService logService;

	@Pointcut("@annotation(io.cms.core.configure.annotation.LogOperation)")
	public void logPointCut() {
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		int status = 0;
		try {
			// 执行方法
			Object result = point.proceed();
			status = Status.SUCCESS.value();
			return result;
		} catch (Exception e) {
			status = Status.FAILURE.value();
			throw e;
		} finally {
			// 执行时长(毫秒)
			long time = System.currentTimeMillis() - beginTime;
			// 保存日志
			saveLog(point, time, status);
		}
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time, Integer status) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		Log log = new Log();
		LogOperation annotation = method.getAnnotation(LogOperation.class);
		if (annotation != null) {
			// 注解上的描述
			log.setOperation(annotation.value());
		}

		// 登录用户信息
		UserDetails user = null;
		try {
			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user != null) {
				log.setCreator(user.getUsername());
			}
		} catch (Exception e) {
		}

		log.setStatus(status);
		log.setRequestTime((int) time);

		// 请求相关信息
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		log.setIp(IpUtils.getIpAddr(request));
		log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		log.setRequestUri(request.getRequestURI());
		log.setRequestMethod(request.getMethod());

		// 请求参数
		Object[] args = joinPoint.getArgs();
		try {
			String params = gson.toJson(args[0]);
			log.setRequestParams(params);
		} catch (Exception e) {
		}

		logService.save(log);
	}
}
