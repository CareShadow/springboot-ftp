package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.annotations.Auth;
import com.example.constants.HttpStatusEnum;
import com.example.constants.UploadConstants;
import com.example.entity.FileStore;
import com.example.entity.MyFile;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.pojo.Result;
import com.example.pojo.UserDTO;
import com.example.service.*;
import com.example.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AdminController
 * @Description TODO
 * @Author admin
 * @Date 2022/2/10 20:46
 * @Version 1.0
 **/
@Controller
@Slf4j
@RequestMapping(value = "/management")
@Auth(id = 2000, name = "用户管理")
public class AdminController {

    @Resource
    private UserService userService;
    @Resource
    private MyFileService myFileService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private ApplicationConfigService applicationConfigService;
    @Resource
    private FileStoreService fileStoreService;

    /**
     * @Description TODO
     * @Param []       
     * @Return com.example.pojo.Result<java.util.Map>
     * @Date 2023/5/26 20:30
     * @Author 18451       
     * @Version 1.0
     **/
    @GetMapping(value = "/admin/index")
    @ResponseBody
    public Result<Map> index() {
        Map<String, Object> result = new HashMap<>();
        User currentUser = RequestContext.getCurrentUser();
        // 图片数量
        int imageFiles = myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getType, 1).eq(MyFile::getUserId, currentUser.getUserId()));
        int videoFiles = myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getType, 2).eq(MyFile::getUserId, currentUser.getUserId()));
        int audioFiles = myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getType, 7).eq(MyFile::getUserId, currentUser.getUserId()));
        int otherFiles = myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getType, 8).eq(MyFile::getUserId, currentUser.getUserId()));
        int officeFiles = myFileService.count(new QueryWrapper<MyFile>().lambda().eq(MyFile::getUserId, currentUser.getUserId())) - imageFiles - videoFiles - audioFiles - otherFiles;
        result.put("imageFiles", imageFiles);
        result.put("videoFiles", videoFiles);
        result.put("audioFiles", audioFiles);
        result.put("otherFiles", otherFiles);
        result.put("officeFiles", officeFiles);
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, result);
    }

    @GetMapping(value = "/admin/category")
    @ResponseBody
    public Result<Map> getStore() {
        Map<String, Object> result = new HashMap<>();
        User currentUser = RequestContext.getCurrentUser();
        FileStore fileStore = fileStoreService.
                getOne(new QueryWrapper<FileStore>().lambda().eq(FileStore::getUserId, currentUser.getUserId()));
        result.put("currentSize", fileStore.getCurrentSize());
        result.put("maxSize", fileStore.getMaxSize());
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, result);
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
        String[] roles;
        if(JwtUtil.parse(token).getSubject().equals("admin")){
            roles = new String[]{"admin"};
        }else {
            roles = new String[]{"editor"};
        }
        result.put("name", JwtUtil.parse(token).getSubject());
        result.put("roles", roles);
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
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
    public Result<Map> verifyLogin(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        log.debug("username: {}, password: {}", username, password);
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
}
