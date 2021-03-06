package com.example.service;

import com.example.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
public interface ResourceService extends IService<Resource> {
    Integer deleteResource();
    Set<String> getAllPaths();
    Set<String> getPathsByUserId(Long id);
}
