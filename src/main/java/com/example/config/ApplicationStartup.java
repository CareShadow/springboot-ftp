package com.example.config;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.annotations.Auth;
import com.example.entity.Resource;
import com.example.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;


import java.util.*;

/**
 * @ClassName ApplicationStartup
 * @Description TODO
 * @Author admin
 * @Date 2022/5/23 14:25
 * @Version 1.0
 **/
@Component
public class ApplicationStartup implements ApplicationRunner {
    @Autowired
    private ResourceService resourceService;
    // RequestMappingHandlerMapping,这个类可以拿到所有你声明web接口信息
    @Autowired
    private RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Resource> list = getAuthResource();
        resourceService.deleteResource();
        if(list.isEmpty()){
            return;
        }
        resourceService.saveBatch(list);
    }

    private List<Resource> getAuthResource(){
        List<Resource> list = new ArrayList<>();
        // 拿到所有接口数据开始遍历
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingInfoHandlerMapping.getHandlerMethods();
        handlerMethods.forEach((info, handlerMethod)->{
            // 类模块 自定义注解信息
            Auth moduleAuth = handlerMethod.getBeanType().getAnnotation(Auth.class);
            // 方法模块 自定义接口信息
            Auth methodAuth = handlerMethod.getMethod().getAnnotation(Auth.class);
            // 缺一不可
            if(moduleAuth == null || methodAuth == null){
                return;
            }
            // 拿到接口方法的请求方式
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            if(methods.size()!=1) return;
            String path = methods.toArray()[0]+":"+info.getPatternsCondition().getPatterns().toArray()[0];
            // 将权限名,资源名
            Resource resource = new Resource();
            resource.setPath(path)
                    .setName(methodAuth.name())
                    .setId(moduleAuth.id()+methodAuth.id());
            list.add(resource);
        });
        return list;
    }
}
