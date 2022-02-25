package com.example.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * 功能描述：
 * @param:
 * @return:
 * @auther: lxl
 * @date: 2022/2/10 16:14
 */
public class FtpUtils {
    /**
     * FTP服务器hostname
     */
    private static String HOST = "120.76.202.102";
    /**
     * FTP服务器端口
     */
    private static int PORT = 21;
    /**
     * FTP登录账号
     */
    private static String USERNAME = "ftptest";
    /**
     * FTP登录密码
     */
    private static String PASSWORD = "password";
    /**
     * FTP服务器基础目录
     */
    private static String BASEPATH = "/var/ftp/test";
    /**
     * FTP客户端
     */
    private static FTPClient ftp;

    /**
     * 功能描述：初始化FtpClient
     * @param: []
     * @return: boolean
     * @auther: lxl
     * @date: 2022/2/10 19:44
     */
    public static boolean initFtpClient(){
        ftp = new FTPClient();
        int reply;
        try {
            // 连接FTP服务器
            ftp.connect(HOST, PORT);
            //登录, 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(USERNAME, PASSWORD);
            ftp.setBufferSize(10240);
            //设置传输超时时间为60秒
            ftp.setDataTimeout(600000);
            //连接超时为60秒
            ftp.setConnectTimeout(600000);
            //FTP以二进制形式传输
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Description: 向FTP服务器上传文件
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input 本地要上传的文件的 输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String filePath, String filename, InputStream input) {
        try {
            filePath = new String(filePath.getBytes("UTF-8"),"iso-8859-1");
            filename = new String(filename.getBytes("UTF-8"),"iso-8859-1");
            if (!initFtpClient()){
                return false;
            };
            //切换到上传目录
            ftp.enterLocalPassiveMode();
            if (!ftp.changeWorkingDirectory(BASEPATH+filePath)) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = BASEPATH;
                //只能逐级创建目录
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)){
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return false;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(BASEPATH+filePath);
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            ftp.enterLocalPassiveMode();
            if (!ftp.storeFile(filename, input)) {
                return false;
            }
            input.close();
            ftp.logout();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return true;
    }

    /**
     * Description: 从FTP服务器下载文件
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @return
     */
    public static boolean downloadFile( String remotePath,String fileName,String localPath) {
        boolean result = false;

        try {
            remotePath = new String(remotePath.getBytes("UTF-8"),"iso-8859-1");
            fileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");
            if (!initFtpClient()){
                return result;
            };
            // 转移到FTP服务器目录
            ftp.changeWorkingDirectory(remotePath);
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    ftp.enterLocalPassiveMode();
                    FileOutputStream outputStream = new FileOutputStream(new File(localPath));
                    ftp.retrieveFile(remotePath+"/"+fileName,outputStream);

                    result = true;
                    outputStream.close();
                    break; //跳出迭代
                }
            }
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * @Description 从ftp服务器下载文件到指定输出流
     * @Author lxl
     * @Date 22:30 2020/3/5
     * @Param [remotePath, fileName, outputStream]
     * @return boolean
     **/
    public static boolean downloadFile(String remotePath, String fileName, OutputStream outputStream) {
        boolean result = false;
        try {
            remotePath = new String(remotePath.getBytes("UTF-8"),"iso-8859-1");
            fileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");
            if (!initFtpClient()){
                return result;
            };
            // 转移到FTP服务器目录
            ftp.changeWorkingDirectory(BASEPATH+remotePath);
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    ftp.enterLocalPassiveMode();
                    ftp.retrieveFile(fileName,outputStream);
                    result = true;
                    break;
                }
            }
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * @Description 删除文件
     * @Author lxl
     * @Date 11:38 2020/2/6
     * @Param [remotePath, fileName]
     * @return void
     **/
    public static boolean deleteFile( String remotePath,String fileName){
        boolean flag = false;
        try {
            remotePath = new String(remotePath.getBytes("UTF-8"),"iso-8859-1");
            fileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");
            if (!initFtpClient()){
                return flag;
            };
            // 转移到FTP服务器目录
            ftp.changeWorkingDirectory(BASEPATH+remotePath);
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if ("".equals(fileName)){
                    return flag;
                }
                if (ff.getName().equals(fileName)){
                    String filePath = BASEPATH+remotePath + "/" +fileName;
                    ftp.deleteFile(filePath);
                    flag = true;
                }
            }
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return flag;
    }

    /**
     * @Description 删除文件夹
     * @Author lxl
     * @Date 11:38 2020/2/6
     * @Param [remotePath, fileName]
     * @return void
     **/
    public static boolean deleteFolder(String remotePath){
        boolean flag = false;
        try {
            remotePath = new String(remotePath.getBytes("UTF-8"),"iso-8859-1");
            if (!initFtpClient()){
                return flag;
            };
            // 转移到FTP服务器目录
            ftp.changeWorkingDirectory(BASEPATH+remotePath);
            ftp.enterLocalPassiveMode();
            //迭代法
            FTPFile[] fs = ftp.listFiles();
            if (fs.length==0){
                 ftp.removeDirectory(BASEPATH+remotePath);
                flag = true;
            }
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return flag;
    }

    /**
     * @Description 修改文件名称或者文件夹名
     * @Author xw
     * @Date 21:18 2020/2/11
     * @Param [oldAllName, newAllName, remotePath]
     * @return boolean
     **/
    public static boolean reNameFile(String oldAllName,String newAllName,String remotePath){
        boolean flag = false;
        try {
            oldAllName = new String(oldAllName.getBytes("UTF-8"),"iso-8859-1");
            newAllName = new String(newAllName.getBytes("UTF-8"),"iso-8859-1");
            if (!initFtpClient()){
                return flag;
            };
            ftp.changeWorkingDirectory(BASEPATH+remotePath);
            ftp.enterLocalPassiveMode();
            flag = ftp.rename(oldAllName, newAllName);
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return flag;
    }
    /***
     * 功能描述：创建文件夹
     * @param: [folderName, parentFolder]
     * @return: boolean
     * @auther: lxl
     * @date: 2022/2/10 19:56
     */
    public static boolean creatFolder(String folderName,String parentFolder){
        try {
        folderName = new String(folderName.getBytes("UTF-8"),"iso-8859-1");
        parentFolder = new String(parentFolder.getBytes("UTF-8"),"iso-8859-1");
        if(!initFtpClient()){
            return false;
        }
            ftp.changeWorkingDirectory(BASEPATH+parentFolder);
            ftp.enterLocalPassiveMode();
            if(!ftp.makeDirectory(folderName)){
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static void main(String[] args) throws Exception{
        System.out.println(FtpUtils.reNameFile("shadow.jpg","nihao.jpg",""));
    }
}
