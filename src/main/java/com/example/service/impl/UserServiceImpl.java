package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.UserMapper;
import com.example.entity.User;
import com.example.pojo.ResourceVO;
import com.example.pojo.RoleVO;
import com.example.pojo.UserVO;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Override
    public List<ResourceVO> getRoleMapResource() {
       return userMapper.getRoleMapResource();
    }

    @Override
    public List<UserVO> getAllUser() {
        return userMapper.getAllUser();
    }

    @Override
    public List<RoleVO> getAllRoleName() {
        return userMapper.getAllRoleName();
    }
}
