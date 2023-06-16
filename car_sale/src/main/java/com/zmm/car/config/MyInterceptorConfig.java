package com.zmm.car.config;

import com.zmm.car.interceptor.JwtValidateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtValidateInterceptor jwtValidateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(jwtValidateInterceptor);
        registration.addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/info",
                        "/user/logout",
                        "/user/register",
                        "/user/getUserByUsername",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/**",
                        "/file/upload",
                        "/file/ossUpload",
                        "/tinymce/upload"
                );
    }
}