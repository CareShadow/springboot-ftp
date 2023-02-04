/*
Navicat MySQL Data Transfer

Source Server         : liang
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : filecontroller

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2022-05-25 18:04:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `application_config`
-- ----------------------------
DROP TABLE IF EXISTS `application_config`;
CREATE TABLE `application_config` (
  `config_field` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '字段名',
  `config_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置名',
  `config_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '配置项的值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`config_field`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of application_config
-- ----------------------------
INSERT INTO `application_config` VALUES ('sysAuthor', '开发者', 'shadow', '2019-08-24 20:33:17', '2019-08-30 03:27:35');
INSERT INTO `application_config` VALUES ('sysAuthorImg', '开发者头像', '/authorImg/20220201_20475968.jpg', '2019-08-24 20:33:14', '2019-08-24 21:56:23');
INSERT INTO `application_config` VALUES ('sysCopyRight', '版权所有', 'xuebingsi(xuebingsi) 访问官网', '2019-08-24 20:33:31', '2019-08-24 11:58:06');
INSERT INTO `application_config` VALUES ('sysEmail', '开发者邮箱', 'careshadow@163.com', '2019-08-24 14:06:48', '2019-08-24 14:06:51');
INSERT INTO `application_config` VALUES ('sysUpdateTime', '最后修改时间', '2019-08-24 20:33:23', '2019-08-24 20:33:20', '2019-08-24 21:56:23');
INSERT INTO `application_config` VALUES ('sysUrl', '服务器url', 'localhost:80', '2019-08-24 14:03:23', '2019-08-24 14:03:26');
INSERT INTO `application_config` VALUES ('sysVersion', '当前版本号', '1.1.0', '2019-08-24 20:33:23', '2019-08-24 11:58:06');

-- ----------------------------
-- Table structure for `file_folder`
-- ----------------------------
DROP TABLE IF EXISTS `file_folder`;
CREATE TABLE `file_folder` (
  `file_folder_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件夹ID',
  `file_folder_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '鏂囦欢澶瑰悕绉?',
  `parent_folder_id` int(11) NOT NULL DEFAULT '0' COMMENT '鐖舵枃浠跺すID',
  `time` datetime NOT NULL COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`file_folder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of file_folder
-- ----------------------------
INSERT INTO `file_folder` VALUES ('1', 'new', '0', '2022-02-11 20:59:21');
INSERT INTO `file_folder` VALUES ('3', 'imgs', '0', '2022-02-11 21:01:16');
INSERT INTO `file_folder` VALUES ('4', 'shadow', '0', '2022-02-24 04:44:13');
INSERT INTO `file_folder` VALUES ('5', 'imgs', '4', '2022-02-24 04:46:08');
INSERT INTO `file_folder` VALUES ('6', 'words', '5', '2022-02-24 04:52:05');
INSERT INTO `file_folder` VALUES ('7', 'test', '5', '2022-02-24 04:56:30');
INSERT INTO `file_folder` VALUES ('8', 'admin', '4', '2022-02-24 10:18:31');
INSERT INTO `file_folder` VALUES ('9', 'file', '8', '2022-02-24 10:18:50');
INSERT INTO `file_folder` VALUES ('10', 'dao', '4', '2022-02-24 10:26:06');

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
  `download_time` int(11) DEFAULT '0' COMMENT '下载次数',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `parent_folder_id` int(11) DEFAULT NULL COMMENT '父文件夹ID',
  `size` int(11) DEFAULT NULL COMMENT '文件大小',
  `type` int(11) DEFAULT NULL COMMENT '文件类型',
  `postfix` varchar(255) DEFAULT NULL COMMENT '文件后缀',
  PRIMARY KEY (`my_file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of my_file
-- ----------------------------
INSERT INTO `my_file` VALUES ('1', 'test2', '1', '2022-02-11 21:03:21', '3', '32', '1', '.jpg');
INSERT INTO `my_file` VALUES ('2', 'fadf8f4844ebceedaa41956a889608b5', '2', '2022-02-11 21:04:52', '3', '32', '1', '.jpg');
INSERT INTO `my_file` VALUES ('3', '113120190309梁细亮', '2', '2022-02-11 21:05:44', '3', '32', '2', '.pdf');
INSERT INTO `my_file` VALUES ('5', 'small03da9b91780405dd5c7ee444658a021c1637856571', '5', '2022-02-20 11:51:48', '1', '82', '1', '.jpg');
INSERT INTO `my_file` VALUES ('6', 'shadow', '4', '2022-02-20 11:52:14', '0', '112', '1', '.jpg');
INSERT INTO `my_file` VALUES ('8', 'download', '1', '2022-02-23 05:17:39', '0', '14', '3', '.zip');
INSERT INTO `my_file` VALUES ('11', 'Mybatis-默认支持的别名', '1', '2022-02-23 06:13:13', '0', '44', '2', '.docx');
INSERT INTO `my_file` VALUES ('12', 'Mybatis-settings', '0', '2022-02-23 09:16:38', '1', '45', '2', '.docx');
INSERT INTO `my_file` VALUES ('13', 'jQuery学习', '5', '2022-02-23 11:51:19', '1', '2', '2', '.txt');
INSERT INTO `my_file` VALUES ('14', 'spring', '1', '2022-02-23 11:53:26', '3', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('15', 'java反射和Java注解', '1', '2022-02-23 12:07:14', '1', '3', '2', '.txt');
INSERT INTO `my_file` VALUES ('16', 'spring', '3', '2022-02-23 12:19:13', '1', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('18', 'spring', '1', '2022-02-23 12:34:30', '0', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('19', 'java基础学习', '2', '2022-02-24 01:19:51', '3', '10', '2', '.txt');
INSERT INTO `my_file` VALUES ('20', 'spring', '2', '2022-02-24 04:44:30', '4', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('22', 'spring', '0', '2022-02-24 10:29:45', '9', '7', '2', '.txt');
INSERT INTO `my_file` VALUES ('24', 'smalled33d7fdd2aa9933aa7c2d325078204e1635128020', '3', '2022-02-24 11:36:49', '3', '114', '1', '.jpg');
INSERT INTO `my_file` VALUES ('25', 'rabbitmq_delayed_message_exchange-3.8.0', '0', '2022-02-25 06:28:07', '1', '42', '3', '.ez');
INSERT INTO `my_file` VALUES ('29', 'Nginx', '1', '2022-05-24 01:20:38', '0', '3119', '2', '.pdf');

-- ----------------------------
-- Table structure for `resource`
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` bigint(20) NOT NULL,
  `path` varchar(255) COLLATE utf8_bin NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES ('1001', 'GET:/admin/v1/folder/create', '创建文件夹');
INSERT INTO `resource` VALUES ('1002', 'GET:/admin/v1/folder/rename', '修改文件夹名');
INSERT INTO `resource` VALUES ('1003', 'GET:/admin/v1/folder/delete', '删除文件夹');
INSERT INTO `resource` VALUES ('1004', 'GET:/admin/v1/file/rename', '修改文件名');
INSERT INTO `resource` VALUES ('1005', 'GET:/admin/v1/file/delete', '删除文件');
INSERT INTO `resource` VALUES ('1006', 'GET:/admin/v1/file/download', '下载文件');
INSERT INTO `resource` VALUES ('1007', 'GET:/admin/v1/file/preview', '预览文件');
INSERT INTO `resource` VALUES ('1008', 'POST:/admin/v1/file/upload', '上传文件');
INSERT INTO `resource` VALUES ('2001', 'GET:/admin/v1/user', '用户页面');
INSERT INTO `resource` VALUES ('2002', 'GET:/admin/v1/user/list', '用户列表');
INSERT INTO `resource` VALUES ('2003', 'GET:/admin/v1/user/role', '用户权限');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员');
INSERT INTO `role` VALUES ('2', '正常用户');
INSERT INTO `role` VALUES ('3', '违规用户');
INSERT INTO `role` VALUES ('4', '游客');

-- ----------------------------
-- Table structure for `role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `role_resource`;
CREATE TABLE `role_resource` (
  `role_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`resource_id`),
  KEY `resource_id` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of role_resource
-- ----------------------------
INSERT INTO `role_resource` VALUES ('1', '1001');
INSERT INTO `role_resource` VALUES ('2', '1001');
INSERT INTO `role_resource` VALUES ('1', '1002');
INSERT INTO `role_resource` VALUES ('2', '1002');
INSERT INTO `role_resource` VALUES ('1', '1003');
INSERT INTO `role_resource` VALUES ('1', '1004');
INSERT INTO `role_resource` VALUES ('2', '1004');
INSERT INTO `role_resource` VALUES ('1', '1005');
INSERT INTO `role_resource` VALUES ('1', '1006');
INSERT INTO `role_resource` VALUES ('2', '1006');
INSERT INTO `role_resource` VALUES ('3', '1006');
INSERT INTO `role_resource` VALUES ('4', '1006');
INSERT INTO `role_resource` VALUES ('1', '1007');
INSERT INTO `role_resource` VALUES ('2', '1007');
INSERT INTO `role_resource` VALUES ('4', '1007');
INSERT INTO `role_resource` VALUES ('1', '1008');
INSERT INTO `role_resource` VALUES ('2', '1008');
INSERT INTO `role_resource` VALUES ('4', '1008');
INSERT INTO `role_resource` VALUES ('1', '2001');
INSERT INTO `role_resource` VALUES ('1', '2002');
INSERT INTO `role_resource` VALUES ('1', '2003');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '閻劍鍩汭D',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '鐢ㄦ埛鍚?',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '鐎靛棛鐖?',
  `register_time` datetime NOT NULL COMMENT '娉ㄥ唽鏃堕棿',
  `image_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '澶村儚鍦板潃',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '5f4dcc3b5aa765d61d8327deb882cf99', '2022-02-11 13:45:19', '/authorImg/20220225_14263870.jpg');
INSERT INTO `user` VALUES ('2', 'shadow', '3bf1114a986ba87ed28fc1b5884fc2f8', '2022-02-25 10:26:30', '/authorImg/20220225_18273654.jpg');
INSERT INTO `user` VALUES ('3', 'gala', 'd2d9ce665f66ae49556b47436a514c0f', '2022-05-24 12:18:22', '');

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) unsigned NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
INSERT INTO `user_role` VALUES ('2', '2');
INSERT INTO `user_role` VALUES ('3', '4');
