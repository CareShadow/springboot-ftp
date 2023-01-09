package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.dao.ResourceMapper;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.dao.UserRoleMapper;
import com.example.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    @Autowired(required = false)
    private ResourceMapper resourceMapper;

    @Override
    @CachePut(cacheNames = "user", key = "#p0")
    public Set<String> updateUserRole(Long userId, Long roleId) {
        int update = userRoleMapper.update(null, new UpdateWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId)
                .set(UserRole::getRoleId, roleId));
        Set<String> pathsByUserId = new HashSet<String>();
        if(update > 0) {
            pathsByUserId = resourceMapper.getPathsByUserId(userId);
        }
        return pathsByUserId;
    }
}
