package com.hello.project.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class Swagger2Configure {

	@Bean
	public Docket createRestApi() {
		
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select().apis(RequestHandlerSelectors.basePackage("com.hello.project"))
				.paths(PathSelectors.any())
                .build();
		
		return docket;
	}
	
	/**
	 * 
	 * @return
	 */
	private ApiInfo apiInfo() {
	     return new ApiInfoBuilder()
	     .title("Spring Boot中使用Swagger2构建RESTful API文档")
	     .description("Spring Boot中使用Swagger2构建RESTful APIs")
	     .termsOfServiceUrl("http://localhost:8077/")
	     .contact("融创软通")
	     .version("1.0")
	     .build();
	 }
	
}
