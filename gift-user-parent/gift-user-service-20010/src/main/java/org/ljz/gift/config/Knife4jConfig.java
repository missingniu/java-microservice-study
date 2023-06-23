package org.ljz.gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2  //2.x用 @EnableSwagger2WebMvc
public class Knife4jConfig {

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("用户中心接口文档")
                .description("用户中心接口文档，仅限内部使用")
                .termsOfServiceUrl("http://www.ljz.org")
                .contact(new Contact("ljz", "www.ljz.org", "169139@qq.com"))
                .version("1.0")
                .build();
    }

    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                //.groupName("1.0版本")
                .select()
                //指定controller（接口）扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("org.ljz.gift.controller"))
                .paths(PathSelectors.any())
                .build();
    }

}