package io.cms.core.datasource.annotation;

import java.lang.annotation.*;

/**
 * DataSource.class
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

	public enum DataSources {
		PRIMARY, SECONDARY
	};

	// DataSources value() default DataSources.WRITE;
	DataSources value();
}
