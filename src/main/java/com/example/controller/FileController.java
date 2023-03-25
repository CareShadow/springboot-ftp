package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.annotations.Auth;
import com.example.constants.HttpStatusEnum;
import com.example.entity.FileFolder;
import com.example.entity.MyFile;
import com.example.pojo.FileVO;
import com.example.pojo.FolderMap;
import com.example.pojo.Result;
import com.example.pojo.TableResultVO;
import com.example.service.FileFolderService;
import com.example.service.MyFileService;
import com.example.utils.*;
import io.minio.ObjectWriteResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName FileController
 * @Description TODO
 * @Author admin
 * @Date 2022/2/13 14:35
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/management")
@Auth(id = 1000, name = "文件管理")
public class FileController {
    @Resource
    private MyFileService myFileService;
    @Resource
    private FileFolderService fileFolderService;
    @Resource
    private FilePathUtils filePathUtils;

    @Resource
    private MinioUtils minioUtils;
    /**
     * @Description TODO
     * @Param [folderId]
     * @Return com.example.pojo.Result<java.util.List<com.example.pojo.FileVO>>
     * @Date 2023/3/21 20:49
     * @Author CareShadow
     * @Version 1.0
     **/
    @GetMapping("/file/list")
    @ResponseBody
    public Result<List<FileVO>> getFileOrFolder(Integer folderId) {
        List<FileVO> fileList = myFileService.getFileList(folderId);
        List<FileVO> folderList = fileFolderService.getFolderList(folderId);
        folderList.addAll(fileList);
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, folderList);
    }

    /**
     * @Description 创建新文件夹
     * @Param [parentFolderId, folderName]
     * @Return com.example.pojo.Result<io.minio.ObjectWriteResponse>
     * @Date 2023/3/24 21:14
     * @Author CareShadow
     * @Version 1.0
     **/
    @GetMapping(value = "/folder/create")
    @Auth(id = 1, name = "创建文件夹")
    @ResponseBody
    public Result<Object> createNewFolder(Integer parentFolderId, String folderName) throws Exception{
        FileFolder fileFolder = FileFolder.builder()
                .parentFolderId(parentFolderId)
                .fileFolderName(folderName)
                .time(new Date())
                .build();
        boolean isSaved = fileFolderService.save(fileFolder);

        if(isSaved) {
            // 在MinIO服务器创建路径及文件夹
            // 获取文件夹路径
            String folderPath = fileFolderService.getFolderPath(parentFolderId) + folderName + "/";
            ObjectWriteResponse response = minioUtils.createFolderPath(folderPath);
            return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, "创建成功", response);
        }
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, "创建失败", isSaved);
    }


    /**
     * 功能描述：修改文件夹名称
     *
     * @param: [folderId, folderName]
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/14 20:05
     */
    @GetMapping(value = "/v1/folder/rename")
    @Auth(id = 2, name = "修改文件夹名")
    @ResponseBody
    public Result<String> renameFolder(Integer folderId, String folderName, String oldName) {
        //获取父文件id
        Integer parentFolderId = fileFolderService.getById(folderId).getParentFolderId();
        //获取父文件夹路径
        String path = filePathUtils.getFilePath(parentFolderId);
        //修改数据库
        FileFolder fileFolder = FileFolder.builder()
                .fileFolderId(folderId)
                .fileFolderName(folderName)
                .build();
        boolean reNameFile = FtpUtils.reNameFile(oldName.trim(), folderName, path);
        boolean update = false;
        if (reNameFile) {
            update = fileFolderService.updateById(fileFolder);
            if (update) {
                return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, "修改成功", "/admin/v1/file?fileFolderId=" + parentFolderId);
            }
        }
        return ResultGenerator.getResultByHttp(HttpStatusEnum.INTERNAL_SERVER_ERROR, "修改失败");
    }

    /***
     * 功能描述：删除文件夹
     * @param: [folderId]
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/15 13:55
     */
    @GetMapping(value = "/v1/folder/delete")
    @Auth(id = 3, name = "删除文件夹")
    @ResponseBody
    public Result<String> deleteFolder(Integer folderId) {
        //boolean remove = fileFolderService.removeById(folderId);
        //boolean deleteFolder = FtpUtils.deleteFolder(path);
        Queue<Integer> folderQueue = new ArrayDeque<>();  //迭代删除文件夹类数据库类的数据
        List<String> folderPath = new ArrayList<>();//记录要删除的文件夹路径
        folderQueue.offer(folderId);
        while (!folderQueue.isEmpty()) {
            Integer node = folderQueue.poll();
            String fileFolderPath = filePathUtils.getFilePath(node);
            //删除文件夹类的文件
            List<MyFile> list = myFileService.list(new QueryWrapper<MyFile>().lambda().eq(MyFile::getParentFolderId, node));
            boolean removeFile = myFileService.remove(new QueryWrapper<MyFile>().lambda().eq(MyFile::getParentFolderId, node));
            for (MyFile file : list) {
                boolean deleteFile = FtpUtils.deleteFile(fileFolderPath, file.getMyFileName() + file.getPostfix());
            }
            //要文件夹中的文件夹加入到队列中去
            List<FileFolder> fileFolderList = fileFolderService.list(new QueryWrapper<FileFolder>().lambda().eq(FileFolder::getParentFolderId, node));
            for (FileFolder fileFolder : fileFolderList) {
                folderQueue.offer(fileFolder.getFileFolderId());
            }
            //记录要删除的文件夹
            folderPath.add(fileFolderPath);
            //数据库删除文件夹数据
            boolean remove = fileFolderService.removeById(node);
        }
        //Ftp服务器删除文件夹数据
        for (String path : folderPath) {
            boolean deleteFolder = FtpUtils.deleteFolder(path);
        }
        return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "删除成功");
    }

    /**
     * 功能描述：修改文件名
     *
     * @param: [fileId, fileName, oldName] 文件名字+后缀
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/15 14:36
     */
    @GetMapping(value = "/v1/file/rename")
    @Auth(id = 4, name = "修改文件名")
    @ResponseBody
    public Result<String> renameFile(Integer folderId, String folderName, String oldName) {
        //获取文件夹路径
        MyFile temp = myFileService.getById(folderId);
        Integer parentFolderId = temp.getParentFolderId();
        String path = filePathUtils.getFilePath(parentFolderId);
        folderName = folderName + temp.getPostfix();
        oldName = oldName.trim() + temp.getPostfix();
        boolean reNameFile = FtpUtils.reNameFile(oldName, folderName, path);
        if (reNameFile) {
            MyFile myFile = new MyFile();
            myFile.setMyFileId(folderId);
            myFile.setMyFileName(folderName.substring(0, folderName.lastIndexOf(".")));
            boolean update = myFileService.updateById(myFile);
            if (update) {
                return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "修改成功");
            }
        }
        return ResultGenerator.getResultByMsg(HttpStatusEnum.BAD_REQUEST, "修改失败");
    }

    /***
     * 功能描述：删除文件
     * @param: [fileId, fileName] 文件名+后缀
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/15 15:27
     */
    @GetMapping(value = "/v1/file/delete")
    @Auth(id = 5, name = "删除文件")
    @ResponseBody
    public Result<String> deleteFile(Integer folderId) {
        //获取文件路径
        MyFile myFile = myFileService.getById(folderId);
        String path = filePathUtils.getFilePath(myFile.getParentFolderId());
        String fileName = myFile.getMyFileName();
        //Ftp服务器操作
        boolean deleteFile = FtpUtils.deleteFile(path, fileName + myFile.getPostfix());
        //Ftp服务器操作成功后在进行数组库进行操作
        if (deleteFile) {
            boolean remove = myFileService.removeById(folderId);
            if (remove) {
                return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "删除成功");
            }
        }
        return ResultGenerator.getResultByMsg(HttpStatusEnum.BAD_REQUEST, "删除失败");
    }

    /**
     * 功能描述：下载文件
     *
     * @param: [fileId, resp]
     * @return: void
     * @auther: lxl
     * @date: 2022/2/17 20:07
     */
    @GetMapping(value = "/v1/file/download")
    @Auth(id = 6, name = "下载文件")
    public void fileDownload(Integer fileId, HttpServletResponse resp) throws IOException {
        //获取输入流
        OutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
        MyFile myfile = myFileService.getById(fileId);
        //获取下载次数
        int downloadNum = myfile.getDownloadTime();
        myfile.setDownloadTime(downloadNum + 1);
        boolean updateById = myFileService.updateById(myfile);
        String myFilePath = filePathUtils.getFilePath(myfile.getParentFolderId());
        String fileName = myfile.getMyFileName() + myfile.getPostfix();
        //注意先设置Header 在下载文件
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        resp.setContentType("multipart/form-data");//对于二进制、文件数据、非ASCll字符使用
        resp.setCharacterEncoding("UTF-8");
        boolean downloadFile = FtpUtils.downloadFile(myFilePath, fileName, outputStream);
        outputStream.flush();
        outputStream.close();
        System.out.println("下载成功");
    }

    /**
     * 功能描述：预览文件
     *
     * @param: [fileId, resp]
     * @return: void
     * @auther: lxl
     * @date: 2022/2/17 20:13
     */
    @GetMapping(value = "/v1/file/preview")
    @Auth(id = 7, name = "预览文件")
    public void filePreview(Integer fileId, HttpServletResponse resp) throws IOException {
        ServletOutputStream outputStream = resp.getOutputStream();
        MyFile myFile = myFileService.getById(fileId);
        //获取文件父路径
        String myFilePath = filePathUtils.getFilePath(myFile.getParentFolderId());
        String fileName = myFile.getMyFileName() + myFile.getPostfix();
        boolean preview = FtpUtils.downloadFile(myFilePath, fileName, outputStream);
        outputStream.flush();
        outputStream.close();
        System.out.println("预览");
    }

    /**
     * 功能描述：上传文件
     *
     * @param: [files]
     * @return: com.example.pojo.TableResultVO
     * @auther: lxl
     * @date: 2022/2/19 20:35
     */
    @PostMapping(value = "/v1/file/upload")
    @Auth(id = 8, name = "上传文件")
    @ResponseBody
    public TableResultVO fileUpload(@RequestParam("file") MultipartFile[] files, Integer id) {
        TableResultVO tableResultVO = new TableResultVO();
        tableResultVO.setCode(0);
        try {
            //遍历文件数组
            for (MultipartFile file : files
            ) {
                boolean singleFileUpload = singleFileUpload(file, id);
                if (!singleFileUpload) tableResultVO.setCode(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableResultVO;
    }

    /**
     * 上传文件页面显示
     * 功能描述：
     *
     * @param: [parentFolderId, map]
     * @return: java.lang.String
     * @auther: lxl
     * @date: 2022/2/20 19:43
     */
    @GetMapping(value = "/v1/file/upload")
    public String uploadFolder(Integer parentFolderId, Map<String, Object> map) {
        //获取父文件夹为parentFolderId
        List<FileFolder> folderList = fileFolderService.list(new QueryWrapper<FileFolder>().lambda()
                .eq(FileFolder::getParentFolderId, parentFolderId));
        //获取文件夹路径
        List<FolderMap> list = new ArrayList<>();
        Integer id = parentFolderId;
        while (parentFolderId != 0) {
            FileFolder fileFolder = fileFolderService.getById(parentFolderId);
            String fileFolderName = fileFolder.getFileFolderName();
            Integer fileFolderId = fileFolder.getFileFolderId();
            FolderMap folderMap = new FolderMap();
            folderMap.setFolderId(fileFolderId);
            folderMap.setFolderName(fileFolderName);
            list.add(folderMap);
            parentFolderId = fileFolder.getParentFolderId();
        }
        //根目录
        FolderMap folderMap = new FolderMap();
        folderMap.setFolderId(0);
        folderMap.setFolderName("根目录");
        list.add(folderMap);
        Collections.reverse(list);
        map.put("paths", list);
        map.put("folder", id);
        map.put("fileFolder", folderList);
        return "upload-file";
    }

    /**
     * 功能描述：单个文件上传步骤
     *
     * @param: [file, id]
     * @return: void
     * @auther: lxl
     * @date: 2022/2/20 19:44
     */
    public boolean singleFileUpload(MultipartFile file, Integer id) throws IOException {
        //生成文件名
        String suffixName = UploadFileUtils.getSuffixName(file);
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        //上传到ftp服务器
        String folderPath = "";
        if (id != 0) folderPath = filePathUtils.getFilePath(id);
        boolean uploadFile = FtpUtils.uploadFile(folderPath, fileName, inputStream);
        //修改数据库文件
        if (uploadFile) {
            MyFile myFile = new MyFile();
            myFile.setMyFileName(fileName.substring(0, fileName.lastIndexOf(".")));
            myFile.setParentFolderId(id);
            myFile.setDownloadTime(0);
            myFile.setPostfix(suffixName);
            myFile.setUploadTime(new Date());
            myFile.setSize((int) file.getSize() / 1024);
            myFile.setType(getFileType(suffixName));
            boolean save = myFileService.save(myFile);
            if (save) return true;
        }
        //查看是否上传到ftp服务器
        System.out.println(uploadFile);
        return false;
    }

    /**
     * 功能描述：返回上一级
     *
     * @param: [id]
     * @return: java.lang.String
     * @auther: lxl
     * @date: 2022/2/23 18:23
     */
    @GetMapping(value = "/v1/back")
    public String turnBack(Integer id) {
        if (id == 0) return "redirect:/admin/v1/file?fileFolderId=0";
        FileFolder folder = fileFolderService.getById(id);
        //获取父文件id
        Integer parentFolderId = folder.getParentFolderId();
        return "redirect:/admin/v1/file?fileFolderId=" + parentFolderId;
    }

    /**
     * 功能描述：获取文件类型 1.图片 2.文件 3。压缩包 4。视频
     *
     * @param: [type]
     * @return: java.lang.Integer
     * @auther: lxl
     * @date: 2022/2/20 19:44
     */
    public Integer getFileType(String type) {
        if (".chm".equals(type) || ".txt".equals(type) || ".xmind".equals(type) || ".xlsx".equals(type) || ".md".equals(type)
                || ".doc".equals(type) || ".docx".equals(type) || ".pptx".equals(type)
                || ".wps".equals(type) || ".word".equals(type) || ".html".equals(type) || ".pdf".equals(type)) {
            return 2;
        } else if (".bmp".equals(type) || ".gif".equals(type) || ".jpg".equals(type) || ".ico".equals(type) || ".vsd".equals(type)
                || ".pic".equals(type) || ".png".equals(type) || ".jepg".equals(type) || ".jpeg".equals(type) || ".webp".equals(type)
                || ".svg".equals(type)) {
            return 1;
        } else if (".avi".equals(type) || ".mov".equals(type) || ".qt".equals(type)
                || ".asf".equals(type) || ".rm".equals(type) || ".navi".equals(type) || ".wav".equals(type)
                || ".mp4".equals(type) || ".mkv".equals(type) || ".webm".equals(type)) {
            return 4;
        }
        return 3;
    }
}

