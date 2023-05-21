package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.ResourceMapper;
import com.example.entity.Resource;
import com.example.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
@Service
// 用来描述该类中所有方法使用的缓存名称
// @CacheConfig(cacheNames = "user")
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired(required = false)
    private ResourceMapper resourceMapper;

    @Override
    public List<Resource> getAllResource() {
        return resourceMapper.getAllResource();
    }

    @Override
    public Integer deleteResource() {
        return resourceMapper.deleteResource();
    }

    @Override
    // @Cacheable(key = "#root.methodName")
    public Set<String> getAllPaths() {
        return resourceMapper.getAllPaths();
    }

    @Override
    // @Cacheable(key = "#p0")
    public Set<String> getPathsByUserId(Long id) {
        return resourceMapper.getPathsByUserId(id);
    }
}
