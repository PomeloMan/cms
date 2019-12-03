package io.cms.core.configure.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link PreCheckInterceptor.class}
 * @author PomeloMan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreCheck {

	boolean repeatSubmitCheck() default true;
}