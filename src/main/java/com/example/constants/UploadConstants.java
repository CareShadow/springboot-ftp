package com.example.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName UploadConstants
 * @Description TODO
 * @Author admin
 * @Date 2022/2/12 11:07
 * @Version 1.0
 **/
@Component
public class UploadConstants {

    // TODO 这里注意路径，本地和prod的区别，只需要在配置文件中配好，linux部署的时候用prod.yml即可，不需要在改动代码
    // 用户头像默认上传路径
    public static String UPLOAD_AUTHOR_IMG = "/tmp/myblog/authorImg/";
    // 文章图片默认上传路径
    public static String FILE_UPLOAD_DIC = "/tmp/myblog/";
    // 用户头像数据库路径
    public static String SQL_AUTHOR_IMG = "/authorImg/";
    // 文章图片数据库路径
    public static String FILE_SQL_DIC = "/upload/";
    //单个文件上传路径
    public static String UPLOAD_FILE = "/tmp/myblog/files";
    //默认路径
    public static String FILE_DIC = "/files/";

    @Value("${upload.uploadAuthorImg}")
    public void setUploadAuthorImg(String uploadAuthorImg) {
        UploadConstants.UPLOAD_AUTHOR_IMG = uploadAuthorImg;
    }

    @Value("${upload.fileUploadDic}")
    public void setFileUploadDic(String fileUploadDic) {
        UploadConstants.FILE_UPLOAD_DIC = fileUploadDic;
    }

    @Value("${upload.sqlAuthorImg}")
    public void setSqlAuthorImg(String sqlAuthorImg) {
        UploadConstants.SQL_AUTHOR_IMG = sqlAuthorImg;
    }

    @Value("${upload.fileSqlDic}")
    public void setFileSqlDic(String fileSqlDic) {
        UploadConstants.FILE_SQL_DIC = fileSqlDic;
    }
    @Value("${upload.uploadFile}")
    public void setUploadFile(String uploadFile){UploadConstants.UPLOAD_FILE = uploadFile;}
    @Value("${upload.fileDic}")
    public void setFileDic(String fileDic){UploadConstants.FILE_DIC = fileDic;}
}

