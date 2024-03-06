package com.second.hand.trading.server.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @Description
 * @Author:luosonglai
 * @CreateTime:2024/3/613:40
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(apiInfo());
        docket.select().apis(RequestHandlerSelectors.basePackage("com.second.hand.trading.server.controller")).build();
        return docket;
    }

    private ApiInfo apiInfo(){
        //作者信息
        Contact contact = new Contact("ceshi","ceshi123","1260638264@qq.com");
        return new ApiInfo("二手图书交易接口文档","即使再小的帆也能远航","v1.0","ceshi",contact,"ceshi","",new ArrayList<>());
    }


}
