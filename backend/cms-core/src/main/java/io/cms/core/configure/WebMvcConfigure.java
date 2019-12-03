package io.cms.core.configure;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.cms.core.configure.interceptor.PreCheckInterceptor;
import io.cms.core.configure.resolver.CurrentUserHandlerMethodArgumentResolver;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

	@Value("${jwt.header}")
	private String header;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new PreCheckInterceptor());
	}

	/**
	 * <p>
	 * 跨域设置<br/>
	 * Cross-domain setting
	 * </p>
	 * {@link https://spring.io/blog/2015/06/08/cors-support-in-spring-framework}
	 * {@link https://spring.io/guides/gs/rest-service-cors/}
	 * {@link application.properties/endpoints.cors.allowed-xxx}
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").exposedHeaders(header, "Content-Disposition") // Set exposed header for front page to get the header.
		 .allowedOrigins("http://localhost", "*")
		 .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE", "*")
		 .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin",
		 "Access-Control-Request-Method", "Access-Control-Request-Headers", "*")
		 .allowCredentials(true).maxAge(3600);
	}

	/**
	 * <p>
	 * 添加自定参数注释
	 * </p>
	 */
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CurrentUserHandlerMethodArgumentResolver());
    }
}
