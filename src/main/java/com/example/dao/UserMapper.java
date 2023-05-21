package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import com.example.pojo.ResourceVO;
import com.example.pojo.RoleVO;
import com.example.pojo.UserVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxl
 * @since 2022-05-22
 */
public interface UserMapper extends BaseMapper<User> {
    List<UserVO> getAllUser();
    List<RoleVO> getAllRoleName();
    List<ResourceVO> getRoleMapResource();
}
