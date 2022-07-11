package net.shop.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;


@Component
//开启lombok
@Data
//开启SwaggerUI的文档
@EnableOpenApi
public class SwaggerConfiguration {

    /**
     * 对C端用户的接口文档
     * @return
     */
    @Bean
    public Docket webApiDoc(){
        return new Docket(DocumentationType.OAS_30)
                .groupName("用户端接口文档")
                .pathMapping("/")
                //定义是否开启Swagger，false是关闭，可以通过变量去控制，线上情况关闭
                .enable(true)
                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.shop"))
                //正则匹配请求路径，并分配至当前分组
                .paths(PathSelectors.ant("/api/**"))
                .build()
                //新版的设置http头参数
                .globalRequestParameters(globalRequestParameter())
                .globalResponses(HttpMethod.GET,getGlobalResponseMessage())
                .globalResponses(HttpMethod.POST,getGlobalResponseMessage());
    }

    /**
     * 对管理端用户的接口文档
     * @return
     */
    @Bean
    public Docket adminApiDoc(){
        return new Docket(DocumentationType.OAS_30)
                .groupName("管理端接口文档")
                .pathMapping("/")
                //定义是否开启Swagger，false是关闭，可以通过变量去控制，线上情况关闭
                .enable(true)
                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.shop"))
                //正则匹配请求路径，并分配至当前分组
                .paths(PathSelectors.ant("/admin/**"))
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("电商平台")
                .description("微服务接口文档")
                .contact(new Contact("Wang Xiaohan","https://github.com/LyWangxiaohan/shop-2022","1278571538@qq.com"))
                .version("v0.1")
                .build();
    }

    /**
     * 在大厂中往往会有一些公共的参数，比如说设备、网络环境、app版本号等
     * @return
     */
    private List<RequestParameter> globalRequestParameter(){
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("token")
                .description("登录令牌")
                .in(ParameterType.HEADER)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                //是否是必须的
                .required(false)
                .build());
        return parameters;
    }

    /**
     * 生成通用的响应信息
     * @return
     */
    private List<Response> getGlobalResponseMessage(){
        List<Response> list = new ArrayList<>();
        list.add(new ResponseBuilder().code("4xx").description("请求错误，根据code和msg检查").build());
        return list;
    }
}
