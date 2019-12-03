package io.cms.core.configure.exception.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import io.cms.core.configure.exception.BusinessException;
import io.cms.core.module.log.persistence.entity.LogError;
import io.cms.core.module.log.service.interfaces.ILogErrorService;
import io.cms.util.HttpContextUtils;
import io.cms.util.http.IpUtils;

/**
 * @ClassName GlobalExceptionHandler.java
 * @Description {@Link BasicErrorController}
 * @author PomeloMan
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Log logger = LogFactory.getLog(GlobalExceptionHandler.class);

	public static final String DEFAULT_ERROR_VIEW = "/error";
	@Autowired
	private Gson gson;
	@Autowired
	private ILogErrorService logErrorService;

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleRenException(BusinessException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public Object defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		Object item = null;
		if (isJson(req)) {
			item = new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("exception", e);
			mav.addObject("url", req.getRequestURI());
			mav.setViewName(DEFAULT_ERROR_VIEW);
			item = mav;
		}
		logger.error(e.getMessage(), e);
		saveErrorLog(e);
		return item;
	}

	private Boolean isJson(HttpServletRequest req) {
		String acceptHeader = req.getHeader("Accept");
		String contentTypeHeader = req.getHeader("Content-Type");
		return (acceptHeader != null && acceptHeader.contains("json"))
				|| (contentTypeHeader != null && contentTypeHeader.contains("json"));
	}

	@SuppressWarnings("unused")
	private boolean isAjax(HttpServletRequest req) {
		return "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
	}

	private void saveErrorLog(Exception e) {
		LogError log = new LogError();
		// 请求相关信息
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		log.setIp(IpUtils.getIpAddr(request));
		log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		log.setRequestUri(request.getRequestURI());
		log.setRequestMethod(request.getMethod());
		Map<String, String> params = HttpContextUtils.getParameterMap(request);
		if (MapUtils.isNotEmpty(params)) {
			log.setRequestParams(gson.toJson(params));
		}
		// 异常信息
		log.setErrorInfo(ExceptionUtils.getStackTrace(e));
		// 保存
		logErrorService.save(log);
	}
}
