package com.ginko.license.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ginko
 * @date 8/6/19
 */

@Configuration//配置类
@EnableSwagger2//启用Swagger
public class SwaggerConfig {

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                // 不使用默认的HTTP响应码
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, buildResponseMessages())
                .globalResponseMessage(RequestMethod.POST, buildResponseMessages())
                // 将范型表示由'«E,T»'改为'OfEAndT'，因为'«E,T»'不符合RFC3986规范
                .forCodeGeneration(true)
                .select()
                // 要扫描的API(Controller)基础包
                .apis(RequestHandlerSelectors.basePackage("com.ginko.license.manager.api.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("证书系统")
                // 详细描述
                .description("证书系统rest-API接口")
                // 版本
                .version("0.0.1")
                .build();
    }

    private List<ResponseMessage> buildResponseMessages() {
        List<ResponseMessage> res = new ArrayList<>();
        ResponseMessage message = new ResponseMessageBuilder()
                .code(200)
                .message("OK").build();
        res.add(message);
        return res;
    }
}
