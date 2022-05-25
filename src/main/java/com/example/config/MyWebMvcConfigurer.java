package com.example.config;

import com.example.constants.UploadConstants;
import com.example.interceptor.AuthInterceptor;
import com.example.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName MyWebMvcConfigurer
 * @Description TODO
 * @Author admin
 * @Date 2022/2/11 14:41
 * @Version 1.0
 **/
@Controller
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        //过滤优先级最高
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Autowired
    private UserLoginInterceptor userLoginInterceptor;
    @Autowired
    private AuthInterceptor authInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/admin/**") //拦截路径
                .excludePathPatterns("/admin/v1/login")
                .excludePathPatterns("/admin/v1/register")
                .excludePathPatterns("/admin/v1/reload")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**")
                .excludePathPatterns("/X-admin/**")
                .order(0);
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/admin/v1/login")
                .excludePathPatterns("/admin/v1/register")
                .order(100);
    }
    //将文件夹绑定到static

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/authorImg/**").addResourceLocations("file:" + UploadConstants.UPLOAD_AUTHOR_IMG);
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + UploadConstants.FILE_UPLOAD_DIC);
        registry.addResourceHandler("/files/**").addResourceLocations("file:"+UploadConstants.UPLOAD_FILE);
    }
}
