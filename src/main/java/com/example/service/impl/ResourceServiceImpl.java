package com.example.service.impl;

import com.example.entity.Resource;
import com.example.dao.ResourceMapper;
import com.example.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired(required = false)
    private ResourceMapper resourceMapper;

    @Override
    public Integer deleteResource() {
        return resourceMapper.deleteResource();
    }

    @Override
    public Set<String> getAllPaths() {
        return resourceMapper.getAllPaths();
    }

    @Override
    public Set<String> getPathsByUserId(Long id) {
        return resourceMapper.getPathsByUserId(id);
    }
}
