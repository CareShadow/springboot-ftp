package com.example.dao;

import com.example.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
