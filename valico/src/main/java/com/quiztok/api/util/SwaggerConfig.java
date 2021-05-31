package com.quiztok.api.util;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.ignoredParameterTypes(HttpSession.class)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())//전체 컨트롤러를 보여준다
				//.apis(RequestHandlerSelectors.basePackage("io.artnote.controller"))
				//.paths(PathSelectors.ant("/*"))//원하는 패키지 안의 특정 url만 활성화 한다
				.build();
	}
}
