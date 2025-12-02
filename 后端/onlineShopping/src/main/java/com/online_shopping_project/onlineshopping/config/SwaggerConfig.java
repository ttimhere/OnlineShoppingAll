package com.online_shopping_project.onlineshopping.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Swagger（OpenAPI）配置类
 * 自动生成项目接口文档，便于接口调试与协作
 */
@Configuration
public class SwaggerConfig {
    //定义 OpenAPI 文档信息
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online Shopping API")  //文档标题
                        .version("1.0") // 文档版本
                        .description("电子商务网站后端接口文档")); //文档描述
    }
}