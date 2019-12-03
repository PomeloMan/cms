package io.cms.core.configure.swagger;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				// 加了 ApiOperation 注解的类，才生成接口文档
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				// 包下的类，才生成接口文档
				// .apis(RequestHandlerSelectors.basePackage("pomelo.core"))
				.paths(PathSelectors.any()).build().securitySchemes(security());// 全局
//				.securityContexts(securityContexts());// 非全局
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("CMS API").description("Restful API specification")
				.termsOfServiceUrl("https://github.com/PomeloMan/pomelo").version("1.0").build();
	}

	private List<ApiKey> security() {
		return Lists.newArrayList(new ApiKey("Authorization", "Authorization", "header"));
	}

//	private List<SecurityContext> securityContexts() {
//		return Lists.newArrayList(SecurityContext.builder().securityReferences(defaultAuth())
//				.forPaths(PathSelectors.regex("^(?!auth).*$")).build());
//	}
//
//	private List<SecurityReference> defaultAuth() {
//		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//		authorizationScopes[0] = authorizationScope;
//		return Lists.newArrayList(new SecurityReference("Authorization", authorizationScopes));
//	}
}
