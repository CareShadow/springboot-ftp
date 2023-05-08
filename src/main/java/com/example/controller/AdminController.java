package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.annotations.Auth;
import com.example.constants.HttpStatusEnum;
import com.example.constants.UploadConstants;
import com.example.entity.MyFile;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.pojo.Result;
import com.example.pojo.TableResultVO;
import com.example.pojo.UserVO;
import com.example.service.ApplicationConfigService;
import com.example.service.MyFileService;
import com.example.service.UserRoleService;
import com.example.service.UserService;
import com.example.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName AdminController
 * @Description TODO
 * @Author admin
 * @Date 2022/2/10 20:46
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/management")
@Auth(id = 2000, name = "用户管理")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private MyFileService myFileService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ApplicationConfigService applicationConfigService;

    /***
     * 功能描述：
     * @param: []
     * @return: java.lang.String
     * @auther: lxl
     * @date: 2022/2/11 11:28
     */
    @GetMapping(value = "/v1/index")
    public String index(HttpSession session) {
        //获取文件,图片,压缩包,视频
        session.setAttribute("imageCount", myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getType, 1)));
        session.setAttribute("fileCount", myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getType, 2)));
        session.setAttribute("archiveCount", myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getType, 3)));
        session.setAttribute("videoCount", myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getType, 4)));
        session.setAttribute("sysList", applicationConfigService.list());
        return "index";
    }
    
    
    /**
     * @Description 用Token获取用户选项
     * @Param [token]
     * @Return com.example.pojo.Result<java.util.Map>
     * @Date 2023/4/24 22:37
     * @Author CareShadow       
     * @Version 1.0
     **/
    @GetMapping("/admin/info")
    @ResponseBody
    public Result<Map> getUserInfoByToken(String token) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", JwtUtil.parse(token).getSubject());
        result.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, result);
    }

    /**
     * @Description 用户退出
     * @Param []
     * @Return com.example.pojo.Result<java.util.Map>
     * @Date 2023/4/24 22:39
     * @Author CareShadow
     * @Version 1.0
     **/
    @PostMapping("/admin/logout")
    @ResponseBody
    public Result<Map> userLogout() {
        Map<String, Object> result = new HashMap<>();
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, result);
    }

    /**
     * 功能描述：
     *
     * @param: [username, password, session]
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/11 21:15
     */
    @ResponseBody
    @PostMapping(value = "/admin/login")
    public Result<Map> verifyLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultGenerator.getResultByHttp(HttpStatusEnum.BAD_REQUEST, null);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(new User().setUserName(username)
                .setPassword(MD5Utils.MD5Encode(password, "UTF-8")));
        User queryUser = userService.getOne(queryWrapper);
        if (queryUser != null) {
            UserRole one = userRoleService.getOne(new QueryWrapper<UserRole>()
                    .lambda().eq(UserRole::getUserId, queryUser.getUserId()));
            String token = JwtUtil.generate(username);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("token", token);
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, result);
        }
        return ResultGenerator.getResultByHttp(HttpStatusEnum.UNAUTHORIZED, null);
    }

    @GetMapping(value = "/v1/userInfo")
    public String UserInfo() {
        return "userInfo-edit";
    }

    /***
     * 功能描述：验证旧密码是否正确
     * @param: [oldPwd]
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/12 11:36
     */
    @ResponseBody
    @GetMapping(value = "/v1/password")
    public Result<String> verifyPassword(String oldPwd, HttpSession session) {
        String username = (String) session.getAttribute("username");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(new User().setUserName(username)
                .setPassword(MD5Utils.MD5Encode(oldPwd, "UTF-8")));
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK);
        } else {
            return ResultGenerator.getResultByHttp(HttpStatusEnum.BAD_REQUEST);
        }
    }

    /**
     * 功能描述：上传用户文件
     *
     * @param: [file]
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/12 13:42
     */
    @ResponseBody
    @PostMapping(value = "/upload/authorImg")
    public Result<String> uploadAuthorImg(HttpSession session, @RequestParam("file") MultipartFile file) {
        String suffixName = UploadFileUtils.getSuffixName(file);
        //生成文件名称通用方法
        String newFileName = UploadFileUtils.getNewFileName(suffixName);
        File fileDirectory = new File(UploadConstants.UPLOAD_AUTHOR_IMG);
        //创建文件
        File destFile = new File(UploadConstants.UPLOAD_AUTHOR_IMG + newFileName);
        try {
            if (!fileDirectory.exists() && !fileDirectory.mkdirs()) {
                throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
            }
            file.transferTo(destFile);
            String sysAuthorImg = UploadConstants.SQL_AUTHOR_IMG + newFileName;//数据库的用户图片路径
            Long id = (Long) session.getAttribute("id");
            User user = new User().setUserId(id).setImagePath(sysAuthorImg);
            session.setAttribute("imagePath", sysAuthorImg);
            userService.updateById(user);
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.getResultByHttp(HttpStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/v1/userInfo")
    public Result<String> updatePwd(String username, String newPwd, HttpSession session) {
        User user = new User().setUserName(username).setPassword(MD5Utils.MD5Encode(newPwd, "UTF-8"));
        boolean flag = userService.updateById(user);
        if (flag) {
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, "/admin/v1/logout");
        } else {
            return ResultGenerator.getResultByHttp(HttpStatusEnum.BAD_REQUEST);
        }
    }

    @ResponseBody
    @GetMapping(value = "/v1/reload")
    public boolean reload(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("id");
        return userId != null && userId != 0;
    }

    @ResponseBody
    @PostMapping("/v1/register")
    public Result<String> registerUser(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultGenerator.getResultByHttp(HttpStatusEnum.BAD_REQUEST);
        }
        User user = new User();
        user.setUserName(username);
        user.setPassword(MD5Utils.MD5Encode(password, "UTF-8"));
        user.setRegisterTime(new Date());
        boolean save = userService.save(user);
        User one = userService.getOne(new QueryWrapper<User>().lambda().eq(User::getUserName, username));
        userRoleService.save(new UserRole().setRoleId((long) 4).setUserId(one.getUserId()));
        if (save) {
            return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "注册成功");
        }
        return ResultGenerator.getResultByMsg(HttpStatusEnum.UNAUTHORIZED, "注册失败");
    }

    @Auth(id = 1, name = "用户页面")
    @GetMapping("/v1/user")
    public String userList() {
        return "user-list";
    }

    @ResponseBody
    @Auth(id = 2, name = "用户列表")
    @GetMapping("/v1/user/list")
    public TableResultVO getUserList() {
        TableResultVO tableResultVO = new TableResultVO();
        Integer count = userService.count();
        //文件在文件夹下面
        List<UserVO> userVOList = userService.getAllUser();
        tableResultVO.setCode(0);
        tableResultVO.setMessage("");
        tableResultVO.setCount(count);
        tableResultVO.setData(userVOList);
        return tableResultVO;
    }

    @GetMapping("/v1/user/role")
    @Auth(id = 3, name = "用户权限")
    @ResponseBody
    public Result<String> updateUser(Long userId, Long roleId) {
        Set<String> paths = userRoleService.updateUserRole(userId, roleId);
        if (paths.size() > 0) {
            return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "修改成功");
        }
        return ResultGenerator.getResultByMsg(HttpStatusEnum.UNAUTHORIZED, "修改失败");
    }
}
