package net.shop.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Component
//开启lombok
@Data
//开启SwaggerUI的文档
@EnableOpenApi
public class SwaggerConfiguration {

    @Bean
    public Docket webApiDoc(){
        return new Docket(DocumentationType.OAS_30)
                .groupName("用户端文档")
                .pathMapping("/")
                //定义是否开启Swagger，false是关闭，可以通过变量去控制，线上情况关闭
                .enable(true)
                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.shop"))
                //正则匹配请求路径，并分配至当前分组
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("电商平台")
                .description("微服务接口文档")
                .contact(new Contact("Wang Xiaohan","https://github.com/LyWangxiaohan/shop-2022","1278571538@qq.com"))
                .version("v0.1")
                .build();
    }
}
