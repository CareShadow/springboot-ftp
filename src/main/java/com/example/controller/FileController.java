package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.annotations.Auth;
import com.example.constants.HttpStatusEnum;
import com.example.entity.FileFolder;
import com.example.entity.MyFile;
import com.example.pojo.FileVO;
import com.example.pojo.FolderMap;
import com.example.pojo.Result;
import com.example.service.FileFolderService;
import com.example.service.MyFileService;
import com.example.utils.FilePathUtils;
import com.example.utils.MinioUtils;
import com.example.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName FileController
 * @Description TODO
 * @Author admin
 * @Date 2022/2/13 14:35
 * @Version 1.0
 **/
@Controller
@Slf4j
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
     * @Description 获取文件目录及文件名字
     * @Param [folderId]
     * @Return com.example.pojo.Result<java.util.List < com.example.pojo.FileVO>>
     * @Date 2023/3/21 20:49
     * @Author CareShadow
     * @Version 1.0
     **/
    @GetMapping("/file/list")
    @ResponseBody
    public Result<Map> getFileOrFolder(Integer folderId) {
        List<FileVO> fileList = myFileService.getFileList(folderId);
        List<FileVO> folderList = fileFolderService.getFolderList(folderId);
        // 文件路径 文件Id做匹配
        List<FolderMap> folderMapList = new ArrayList<>();
        folderMapList.add(FolderMap.builder().FolderName("根目录").FolderId(0).build());
        if (folderId != 0) {
            String[] folderArr = fileFolderService.getMinIOPath(folderId).split("/");
            String[] idArr = fileFolderService.getFolderID(folderId).split("/");
            for (int i = 0; i < folderArr.length; i++) {
                FolderMap build = FolderMap.builder().FolderId(Integer.parseInt(idArr[i])).FolderName(folderArr[i]).build();
                folderMapList.add(build);
            }
        }
        folderList.addAll(fileList);
        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("folderList", folderList);
        resultMap.put("content", folderMapList);
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, resultMap);
    }

    /**
     * @Description TODO
     * @Param [parentFolderId, folderName]       
     * @Return com.example.pojo.Result<java.lang.Object>
     * @Date 2023/4/9 20:24
     * @Author CareShadow       
     * @Version 1.0
     *
     *
     **/
    @GetMapping(value = "/folder/create")
    @Auth(id = 1, name = "创建文件夹")
    @ResponseBody
    public Result<Object> createNewFolder(Integer parentFolderId, String folderName) throws Exception {
      return fileFolderService.createNewFolder(parentFolderId, folderName);
    }


    /**
     * 功能描述：修改文件夹名称
     *
     * @param: [folderId, folderName]
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/14 20:05
     */
    @GetMapping(value = "/folder/rename")
    @Auth(id = 2, name = "修改文件夹名")
    @ResponseBody
    public Result<String> renameFolder(Integer folderId, String folderName) {
        //修改数据库
        FileFolder fileFolder = FileFolder.builder()
                .fileFolderId(folderId)
                .fileFolderName(folderName)
                .build();
        boolean isUpdated = fileFolderService.updateById(fileFolder);
        if (isUpdated) {
            return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "修改成功");
        }
        return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, "修改失败");
    }

    /***
     * 功能描述：删除文件夹
     * @param: [folderId]
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/15 13:55
     */
    @GetMapping(value = "/folder/delete")
    @Auth(id = 3, name = "删除文件夹")
    @ResponseBody
    public Result<String> deleteFolder(Integer folderId) throws Exception {
        Deque<Integer> folderDeque = new ArrayDeque<Integer>();
        List<Integer> fileList = new ArrayList<>();
        List<Integer> folderList = new ArrayList<>();
        folderDeque.add(folderId);
        folderList.add(folderId);
        while (!folderDeque.isEmpty()) {
            Integer folderTemp = folderDeque.pollFirst();
            List<Integer> fileIdTemp = myFileService.list(new QueryWrapper<MyFile>().lambda().eq(MyFile::getParentFolderId, folderTemp))
                    .stream().map(file -> file.getMyFileId()).collect(Collectors.toList());
            // 将要删除的文件放到集合中
            fileList.addAll(fileIdTemp);
            List<Integer> folderIdList = fileFolderService.list(new QueryWrapper<FileFolder>().lambda().eq(FileFolder::getParentFolderId, folderTemp))
                    .stream().map(folder -> folder.getFileFolderId()).collect(Collectors.toList());
            // 将要删除的文件夹放到集合中
            folderList.addAll(folderIdList);
            folderDeque.addAll(folderIdList);
        }
        // 批量删除
        boolean isFileRemoved = myFileService.remove(new QueryWrapper<MyFile>().lambda().in(MyFile::getMyFileId, fileList));
        boolean isFolderRemoved = fileFolderService.remove(new QueryWrapper<FileFolder>().lambda().in(FileFolder::getFileFolderId, folderList));
        // minio删除文件夹
        minioUtils.removeFolder("file", fileFolderService.getFolderPath(folderId));

        String message = isFileRemoved && isFolderRemoved ? "删除成功" : "删除失败";
        return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, message);
    }

    /**
     * 功能描述：修改文件名
     *
     * @param: [fileId, fileName, oldName] 文件名字+后缀
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/15 14:36
     */
    @GetMapping(value = "/file/rename")
    @Auth(id = 4, name = "修改文件名")
    @ResponseBody
    public Result<String> renameFile(Integer fileId, String fileName) {
        //获取文件夹路径
        boolean update = myFileService.update(new UpdateWrapper<MyFile>().lambda().eq(MyFile::getMyFileId, fileId).set(MyFile::getMyFileName, fileName));
        String message = update ? "修改成功" : "修改失败";
        return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, message);
    }

    /***
     * 功能描述：删除文件
     * @param: [fileId, fileName] 文件名+后缀
     * @return: com.example.pojo.Result<java.lang.String>
     * @auther: lxl
     * @date: 2022/2/15 15:27
     */
    @GetMapping(value = "/file/delete")
    @Auth(id = 5, name = "删除文件")
    @ResponseBody
    public Result<String> deleteFile(Integer fileId) throws Exception {
        //获取文件路径
        MyFile file = myFileService.getById(fileId);
        Integer folderId = file.getParentFolderId();
        String path = fileFolderService.getFolderPath(folderId) + file.getMyFileName() + "." +  file.getPostfix();
        String message = "删除失败";
        // 数据库删除
        boolean isDeleted = myFileService.removeById(fileId);
        if (isDeleted) {
            minioUtils.removeObject(path);
            message = "删除成功";
        }
        return ResultGenerator.getResultByMsg(HttpStatusEnum.OK, message);
    }

    /**
     * 功能描述：下载文件
     *
     * @param: [fileId, resp]
     * @return: void
     * @auther: lxl
     * @date: 2022/2/17 20:07
     */
    @GetMapping(value = "/file/download")
    @Auth(id = 6, name = "下载文件")
    public void fileDownload(Integer fileId, HttpServletResponse resp) throws Exception {
        //获取输入流
        OutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
        MyFile myfile = myFileService.getById(fileId);
        //获取下载次数
        int downloadNum = myfile.getDownloadTime();
        myfile.setDownloadTime(downloadNum + 1);
        boolean updateById = myFileService.updateById(myfile);
        // 获取文件夹路径
        String path = fileFolderService.getFolderPath(myfile.getParentFolderId());
        String fileName = myfile.getMyFileName() + "." +myfile.getPostfix();
        //注意先设置Header 在下载文件
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        resp.setContentType("multipart/form-data");//对于二进制、文件数据、非ASCll字符使用
        resp.setCharacterEncoding("UTF-8");
        // boolean downloadFile = FtpUtils.downloadFile(myFilePath, fileName, outputStream);
        InputStream in = minioUtils.getObject("file",path + fileName);
        // 将InputStream转换为OutputStream
        IOUtils.copy(in, outputStream);
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
    @GetMapping(value = "/file/preview")
    @Auth(id = 7, name = "预览文件")
    public Result<String> filePreview(Integer fileId) throws Exception {
        MyFile myFile = myFileService.getById(fileId);
        //获取文件父路径
        String folderPath = fileFolderService.getFolderPath(myFile.getParentFolderId());
        String path = folderPath + myFile.getMyFileName() + "." + myFile.getPostfix();
        // 预览路径 图片,视频,音乐
        String preViewUrl = minioUtils.getPreViewUrl("file", path);
        return ResultGenerator.getResultByHttp(HttpStatusEnum.OK, preViewUrl);
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

