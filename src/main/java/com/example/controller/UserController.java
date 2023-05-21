package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.constants.HttpStatusEnum;
import com.example.entity.FileStore;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.pojo.ResourceVO;
import com.example.pojo.Result;
import com.example.pojo.RoleVO;
import com.example.pojo.UserVO;
import com.example.service.FileStoreService;
import com.example.service.ResourceService;
import com.example.service.UserRoleService;
import com.example.service.UserService;
import com.example.utils.MD5Utils;
import com.example.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
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
    @Resource
    private ResourceService resourceService;
    @Resource
    private FileStoreService fileStoreService;
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

    /**
     * @Description 获取角色的资源列表，显示在角色中去
     * @Param []
     * @Return com.example.pojo.Result<java.util.List < com.example.pojo.ResourceVO>>
     * @Date 2023/5/9 22:29
     * @Author CareShadow
     * @Version 1.0
     **/
    @GetMapping("/user/resource_list")
    @ResponseBody
    public Result<List<ResourceVO>> getRoleMapResource() {
        // 先获取用户角色的Id, Name
        List<ResourceVO> roleMapResource = userService.getRoleMapResource();
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, roleMapResource);
    }

    /**
     * @Description 获取全部资源
     * @Param []
     * @Return com.example.pojo.Result<java.util.List < com.example.entity.Resource>>
     * @Date 2023/5/11 7:34
     * @Author CareShadow
     * @Version 1.0
     **/
    @GetMapping("/user/resource")
    @ResponseBody
    public Result<List<com.example.entity.Resource>> getAllResource() {
        List<com.example.entity.Resource> allResource = resourceService.getAllResource();
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, allResource);
    }


    /**
     * @Description 添加用户
     * @Param []
     * @Return com.example.pojo.Result<java.lang.String>
     * @Date 2023/5/11 7:59
     * @Author CareShadow
     * @Version 1.0
     **/
    @PostMapping("/user/add/{username}")
    @Transactional(isolation = Isolation.DEFAULT, timeout = -1, propagation = Propagation.REQUIRED)
    @ResponseBody
    public Result<String> addUser(@PathVariable("username") String username) {
        if (StringUtils.isEmpty(username)) {
            return ResultGenerator.getResultByHttp(HttpStatusEnum.BAD_REQUEST);
        }
        User entity = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUserName, username));
        if(entity != null) {
            return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "用户名重复");
        }
        User user = new User();
        user.setUserName(username);
        // 默认密码是password
        user.setPassword(MD5Utils.MD5Encode("password", "UTF-8"));
        user.setRegisterTime(new Date());
        boolean save = userService.save(user);
        User one = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUserName, username));
        // 将用户的角色设置成游客
        userRoleService.save(new UserRole().setRoleId((long) 4).setUserId(one.getUserId()));
        //创建一个文件仓库，用于设置文件传输大小
        FileStore fileStore = FileStore.builder()
                .currentSize(0)
                .fileStoreName(username)
                .userId(one.getUserId())
                .maxSize(1048576)
                .build();
        fileStoreService.save(fileStore);
        if (save) {
            return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "添加成功");
        }
        return ResultGenerator.getResultByMsg(HttpStatusEnum.UNAUTHORIZED, "添加失败");
    }
}
