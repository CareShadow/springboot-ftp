package com.example.service.impl;

import com.example.entity.User;
import com.example.dao.UserMapper;
import com.example.pojo.UserVO;
import com.example.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
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
    public List<UserVO> getAllUser() {
        return userMapper.getAllUser();
    }
}
