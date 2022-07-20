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
    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/api/coupon_record/*/**")
                .addPathPatterns("/api/coupon/*/**")

                //不拦截的路径
                .excludePathPatterns("/api/coupon/*/page_coupon");

        WebMvcConfigurer.super.addInterceptors(registry);
    }


}
