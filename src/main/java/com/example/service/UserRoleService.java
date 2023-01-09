package com.example.service;

import com.example.entity.UserRole;
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
public interface UserRoleService extends IService<UserRole> {
    Set<String> updateUserRole(Long userId, Long roleId);
}
