package com.example.Gym_management_system.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ProjectName: hi-store
 * @Titile: StoreWebConfig
 * @Author: Lucky
 * @Description: 配  置类
 */
@SpringBootConfiguration //标注代表这个类是springboot的配置类
public class StoreWebConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new TokenInterceptor()) //注册拦截器
//                .addPathPatterns("/api/user/**","/api/address/**","/api/favorite/**") // 添加拦截的url
//                .excludePathPatterns("/api/user/login","/api/user/reg");//放行的url
//    }

    /**
     * 设置全端跨域请求
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //对所有的消求
                .allowedOriginPatterns("*") //所有的源
                .allowedHeaders("*")//所有的请求义
                .allowedMethods("*") //所有的请求方式
                .allowCredentials(true) //允济验认
                .maxAge(3600); //以秘为单位
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("file:C:/Users/asus/Desktop/demo/idea/Gym_management_system/Gym_management_system/src/main/resources/images/");
    }

}
