package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.constants.HttpStatusEnum;
import com.example.entity.UserRole;
import com.example.pojo.Result;
import com.example.pojo.RoleVO;
import com.example.pojo.UserVO;
import com.example.service.UserRoleService;
import com.example.service.UserService;
import com.example.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author CareShadow
 * @Date 2023/5/1 16:43
 * @Version 1.0
 **/
@Controller
@Slf4j
@RequestMapping(value = "/management")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;

    /**
     * @Description TODO
     * @Param []
     * @Return com.example.pojo.Result<com.example.entity.User>
     * @Date 2023/5/1 16:58
     * @Author CareShadow
     * @Version 1.0
     **/
    @GetMapping(value = "/user/list")
    @ResponseBody
    public Result<List<UserVO>> getAllUser() {
        List<UserVO> userList = userService.getAllUser();
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, userList);
    }


    /**
     * @Description 用户角色的更新
     * @Param [userVO]
     * @Return com.example.pojo.Result<java.lang.String>
     * @Date 2023/5/6 20:31
     * @Author CareShadow
     * @Version 1.0
     **/
    @PostMapping(value = "/user/role_change/{userId}/{roleId}")
    @ResponseBody
    @Transactional(isolation = Isolation.DEFAULT, timeout = -1, propagation = Propagation.REQUIRED)
    public Result<String> updateUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        log.info("用户对象是：{}, {}", userId, roleId);
        boolean isUpdate = userRoleService
                .update(new UpdateWrapper<UserRole>().lambda().eq(UserRole::getUserId, userId).set(UserRole::getRoleId, roleId));
        return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "修改成功");
    }

    /**
     * @Description 获取用户角色列表, 用于选择器的选取
     * @Param []
     * @Return com.example.pojo.Result<java.util.List < com.example.pojo.RoleVO>>
     * @Date 2023/5/8 18:06
     * @Author CareShadow
     * @Version 1.0
     **/
    @GetMapping("/user/role_list")
    @ResponseBody
    public Result<List<RoleVO>> getAllRoleName() {
        List<RoleVO> roleList = userService.getAllRoleName();
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, roleList);
    }
}
