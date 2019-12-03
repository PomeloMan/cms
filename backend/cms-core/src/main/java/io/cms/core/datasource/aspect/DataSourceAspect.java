package io.cms.core.datasource.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.cms.core.datasource.DynamicDataSource;
import io.cms.core.datasource.annotation.DataSource;
import io.cms.core.datasource.annotation.DataSource.DataSources;

/**
 * DataSource aspect
 * 
 * @author Pomelor
 */
@Aspect
@Component
@Order(Integer.MIN_VALUE)
public class DataSourceAspect {

	private final static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

	@Pointcut("@annotation(io.cms.core.datasource.annotation.DataSource)")
	public void dataSourcePointCut() {
	}

	@Around("dataSourcePointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();

		DataSource datasource = method.getAnnotation(DataSource.class);
		if (datasource == null) {
			DynamicDataSource.setDataSource(DataSources.PRIMARY);
			logger.info("Set datasource: " + DataSources.PRIMARY);
		} else {
			DynamicDataSource.setDataSource(datasource.value());
			logger.info("Set datasource: " + datasource.value());
		}

		try {
			return point.proceed();
		} finally {
			DynamicDataSource.clearDataSource();
			logger.debug("clean datasource");
		}
	}
}
