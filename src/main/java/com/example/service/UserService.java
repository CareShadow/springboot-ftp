package com.example.service;

import com.example.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.RoleVO;
import com.example.pojo.UserVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
public interface UserService extends IService<User> {
    List<UserVO> getAllUser();
    List<RoleVO> getAllRoleName();
}
