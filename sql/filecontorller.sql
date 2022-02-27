/*
Navicat MySQL Data Transfer

Source Server         : liang
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : filecontorller

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2022-02-27 21:22:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `blog_config`
-- ----------------------------
DROP TABLE IF EXISTS `blog_config`;
CREATE TABLE `blog_config` (
  `config_field` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '字段名',
  `config_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置名',
  `config_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '配置项的值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`config_field`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of blog_config
-- ----------------------------
INSERT INTO `blog_config` VALUES ('sysAuthor', '开发者', 'shadow', '2019-08-24 20:33:17', '2019-08-30 03:27:35');
INSERT INTO `blog_config` VALUES ('sysAuthorImg', '开发者头像', '/authorImg/20220201_20475968.jpg', '2019-08-24 20:33:14', '2019-08-24 21:56:23');
INSERT INTO `blog_config` VALUES ('sysCopyRight', '版权所有', 'xuebingsi(xuebingsi) 访问官网', '2019-08-24 20:33:31', '2019-08-24 11:58:06');
INSERT INTO `blog_config` VALUES ('sysEmail', '开发者邮箱', '1320291471@qq.com', '2019-08-24 14:06:48', '2019-08-24 14:06:51');
INSERT INTO `blog_config` VALUES ('sysUpdateTime', '最后修改时间', '2019-08-24 20:33:23', '2019-08-24 20:33:20', '2019-08-24 21:56:23');
INSERT INTO `blog_config` VALUES ('sysUrl', '服务器url', 'localhost:80', '2019-08-24 14:03:23', '2019-08-24 14:03:26');
INSERT INTO `blog_config` VALUES ('sysVersion', '当前版本号', '1.1.0', '2019-08-24 20:33:23', '2019-08-24 11:58:06');

-- ----------------------------
-- Table structure for `file_folder`
-- ----------------------------
DROP TABLE IF EXISTS `file_folder`;
CREATE TABLE `file_folder` (
  `file_folder_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件夹ID',
  `file_folder_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '鏂囦欢澶瑰悕绉?',
  `parent_folder_id` int(11) NOT NULL DEFAULT '0' COMMENT '鐖舵枃浠跺すID',
  `time` datetime NOT NULL COMMENT '鍒涘缓鏃堕棿',
  `file_folder_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`file_folder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of file_folder
-- ----------------------------
INSERT INTO `file_folder` VALUES ('1', 'hello', '0', '2022-02-11 20:59:21', '/hello');
INSERT INTO `file_folder` VALUES ('3', 'imgs', '0', '2022-02-11 21:01:16', '/imgs');
INSERT INTO `file_folder` VALUES ('4', 'shadow', '0', '2022-02-24 04:44:13', '/shadow');
INSERT INTO `file_folder` VALUES ('5', 'imgs', '4', '2022-02-24 04:46:08', '/shadow/imgs');
INSERT INTO `file_folder` VALUES ('6', 'words', '5', '2022-02-24 04:52:05', '/shadow/imgs/words');
INSERT INTO `file_folder` VALUES ('7', 'test', '5', '2022-02-24 04:56:30', '/shadow/imgs/test');
INSERT INTO `file_folder` VALUES ('8', 'admin', '4', '2022-02-24 10:18:31', '/shadow/admin');
INSERT INTO `file_folder` VALUES ('9', 'file', '8', '2022-02-24 10:18:50', '/shadow/admin/file');
INSERT INTO `file_folder` VALUES ('10', 'dao', '4', '2022-02-24 10:26:06', '/shadow/dao');
INSERT INTO `file_folder` VALUES ('11', 'test', '0', '2022-02-24 11:33:03', '/test');

-- ----------------------------
-- Table structure for `file_store`
-- ----------------------------
DROP TABLE IF EXISTS `file_store`;
CREATE TABLE `file_store` (
  `file_store_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件仓库ID',
  `user_id` int(11) DEFAULT NULL COMMENT '主人ID',
  `current_size` int(11) DEFAULT '0' COMMENT '当前容量（单位KB）',
  `max_size` int(11) DEFAULT '1048576' COMMENT '最大容量（单位KB）',
  PRIMARY KEY (`file_store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of file_store
-- ----------------------------

-- ----------------------------
-- Table structure for `my_file`
-- ----------------------------
DROP TABLE IF EXISTS `my_file`;
CREATE TABLE `my_file` (
  `my_file_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `my_file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `my_file_path` varchar(255) DEFAULT '/' COMMENT '文件存储路径',
  `download_time` int(11) DEFAULT '0' COMMENT '下载次数',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `parent_folder_id` int(11) DEFAULT NULL COMMENT '父文件夹ID',
  `size` int(11) DEFAULT NULL COMMENT '文件大小',
  `type` int(11) DEFAULT NULL COMMENT '文件类型',
  `postfix` varchar(255) DEFAULT NULL COMMENT '文件后缀',
  PRIMARY KEY (`my_file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of my_file
-- ----------------------------
INSERT INTO `my_file` VALUES ('1', 'test2', '/imgs/test2.jpg', '0', '2022-02-11 21:03:21', '3', '32', '1', '.jpg');
INSERT INTO `my_file` VALUES ('2', 'fadf8f4844ebceedaa41956a889608b5', '/imgs/fadf8f4844ebceedaa41956a889608b5.jpg', '1', '2022-02-11 21:04:52', '3', '32', '1', '.jpg');
INSERT INTO `my_file` VALUES ('3', '113120190309梁细亮', '/imgs/113120190309梁细亮.pdf', '1', '2022-02-11 21:05:44', '3', '32', '2', '.pdf');
INSERT INTO `my_file` VALUES ('5', 'small03da9b91780405dd5c7ee444658a021c1637856571', '/hello/small03da9b91780405dd5c7ee444658a021c1637856571.jpg', '2', '2022-02-20 11:51:48', '1', '82', '1', '.jpg');
INSERT INTO `my_file` VALUES ('6', 'shadow', '/shadow.jpg', '1', '2022-02-20 11:52:14', '0', '112', '1', '.jpg');
INSERT INTO `my_file` VALUES ('8', 'download', '/download.zip', '1', '2022-02-23 05:17:39', '0', '14', '3', '.zip');
INSERT INTO `my_file` VALUES ('11', 'Mybatis-默认支持的别名', '/Mybatis-默认支持的别名.docx', '1', '2022-02-23 06:13:13', '0', '44', '2', '.docx');
INSERT INTO `my_file` VALUES ('12', 'Mybatis-settings', '/hello/Mybatis-settings.docx', '0', '2022-02-23 09:16:38', '1', '45', '2', '.docx');
INSERT INTO `my_file` VALUES ('13', 'jQuery学习', '/hello/jQuery学习.txt', '4', '2022-02-23 11:51:19', '1', '2', '2', '.txt');
INSERT INTO `my_file` VALUES ('14', 'spring', '/imgs/spring.txt', '0', '2022-02-23 11:53:26', '3', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('15', 'java反射和Java注解', '/hello/java反射和Java注解.txt', '1', '2022-02-23 12:07:14', '1', '3', '2', '.txt');
INSERT INTO `my_file` VALUES ('16', 'spring', '/hello/spring.txt', '0', '2022-02-23 12:19:13', '1', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('18', 'spring', '/spring.txt', '0', '2022-02-23 12:34:30', '0', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('19', 'java基础学习', '/imgs/java基础学习.txt', '0', '2022-02-24 01:19:51', '3', '10', '2', '.txt');
INSERT INTO `my_file` VALUES ('20', 'spring', '/shadow/spring.txt', '0', '2022-02-24 04:44:30', '4', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('22', 'spring', '/shadow/admin/file/spring.txt', '0', '2022-02-24 10:29:45', '9', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('23', 'java基础学习', '/test/java基础学习.txt', '0', '2022-02-24 11:33:30', '11', '10', '2', '.txt');
INSERT INTO `my_file` VALUES ('24', 'smalled33d7fdd2aa9933aa7c2d325078204e1635128020', '/imgs/smalled33d7fdd2aa9933aa7c2d325078204e1635128020.jpg', '2', '2022-02-24 11:36:49', '3', '114', '1', '.jpg');
INSERT INTO `my_file` VALUES ('25', 'rabbitmq_delayed_message_exchange-3.8.0', '/hello/rabbitmq_delayed_message_exchange-3.8.0.ez', '0', '2022-02-25 06:28:07', '1', '42', '3', '.ez');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '瀵嗙爜',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `image_path` varchar(255) DEFAULT '' COMMENT '头像地址',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '5f4dcc3b5aa765d61d8327deb882cf99', '2022-02-11 13:45:19', '/authorImg/20220225_14263870.jpg');
INSERT INTO `user` VALUES ('2', 'shadow', '3bf1114a986ba87ed28fc1b5884fc2f8', '2022-02-25 10:26:30', '/authorImg/20220225_18273654.jpg');
