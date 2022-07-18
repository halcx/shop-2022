package net.shop.config;

import lombok.extern.slf4j.Slf4j;
import net.shop.intercepter.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器的配置
 */
@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {
    LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                //添加拦截的路径
                .addPathPatterns("/api/user/*/**","/api/address/*/**")
                //添加不拦截的路径
                .excludePathPatterns("/api/user/*/send_code","/api/user/*/kapthca",
                        "/api/user/*/register","/api/user/*/login","/api/user/*/upload");
    }
}
