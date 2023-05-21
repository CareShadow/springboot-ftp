package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Resource;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
public interface ResourceMapper extends BaseMapper<Resource> {
    Integer deleteResource();
    Set<String> getAllPaths();
    Set<String> getPathsByUserId(Long id);
    List<Resource> getAllResource();
}
