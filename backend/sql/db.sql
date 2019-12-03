/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : cms

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 03/12/2019 13:00:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_authority
-- ----------------------------
DROP TABLE IF EXISTS `sys_authority`;
CREATE TABLE `sys_authority`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_date` datetime(0) NULL DEFAULT NULL,
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_authority
-- ----------------------------
INSERT INTO `sys_authority` VALUES (1, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'USER', '/user/*');
INSERT INTO `sys_authority` VALUES (2, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'USER_PAGE', '/user/page');
INSERT INTO `sys_authority` VALUES (3, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'USER_LIST', '/user/list');
INSERT INTO `sys_authority` VALUES (4, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'USER_SAVE', '/user/save');
INSERT INTO `sys_authority` VALUES (5, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'USER_DELETE', '/user/delete');
INSERT INTO `sys_authority` VALUES (6, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'ROLE', '/role/*');
INSERT INTO `sys_authority` VALUES (7, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'ROLE_PAGE', '/role/page');
INSERT INTO `sys_authority` VALUES (8, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'ROLE_LIST', '/role/list');
INSERT INTO `sys_authority` VALUES (9, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'ROLE_SAVE', '/role/save');
INSERT INTO `sys_authority` VALUES (10, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'ROLE_DELETE', '/role/delete');
INSERT INTO `sys_authority` VALUES (11, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'AUTH', '/auth/*');
INSERT INTO `sys_authority` VALUES (12, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'AUTH_PAGE', '/auth/page');
INSERT INTO `sys_authority` VALUES (13, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'AUTH_LIST', '/auth/list');
INSERT INTO `sys_authority` VALUES (14, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'AUTH_SAVE', '/auth/save');
INSERT INTO `sys_authority` VALUES (15, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'AUTH_DELETE', '/auth/delete');
INSERT INTO `sys_authority` VALUES (16, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'MENU', '/menu/*');
INSERT INTO `sys_authority` VALUES (17, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'MENU_PAGE', '/menu/page');
INSERT INTO `sys_authority` VALUES (18, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'MENU_LIST', '/menu/list');
INSERT INTO `sys_authority` VALUES (19, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'MENU_SAVE', '/menu/save');
INSERT INTO `sys_authority` VALUES (20, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'MENU_DELETE', '/menu/delete');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典编码',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int(11) NULL DEFAULT NULL COMMENT '字典状态',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `valuei18n` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '字典值（国际化），用|分隔',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'DICT_MENU_SYSTEM', NULL, 1, 'DICT_MENU', '系统管理', 'System Management');
INSERT INTO `sys_dict` VALUES (2, 'DICT_MENU_USER', NULL, 1, 'DICT_MENU', '用户管理', 'User Management');
INSERT INTO `sys_dict` VALUES (3, 'DICT_MENU_ROLE', NULL, 1, 'DICT_MENU', '角色管理', 'Role Management');
INSERT INTO `sys_dict` VALUES (4, 'DICT_MENU_AUTH', NULL, 1, 'DICT_MENU', '权限管理', 'Auth Management');
INSERT INTO `sys_dict` VALUES (5, 'DICT_MENU_MENU', NULL, 1, 'DICT_MENU', '菜单管理', 'Menu Management');
INSERT INTO `sys_dict` VALUES (101, 'DICT_SYSTEM_STATUS_INIT', NULL, 1, 'DICT_SYSTEM_STATUS', '初始化', 'Init');
INSERT INTO `sys_dict` VALUES (102, 'DICT_SYSTEM_STATUS_VALID', NULL, 1, 'DICT_SYSTEM_STATUS', '已生效', 'Valid');
INSERT INTO `sys_dict` VALUES (103, 'DICT_SYSTEM_STATUS_INVALID', NULL, 1, 'DICT_SYSTEM_STATUS', '已失效', 'Invalid');
INSERT INTO `sys_dict` VALUES (104, 'DICT_SYSTEM_STATUS_DELETED', NULL, 1, 'DICT_SYSTEM_STATUS', '已删除', 'Deleted');
INSERT INTO `sys_dict` VALUES (105, 'DICT_SYSTEM_STATUS_EXPIRED', NULL, 1, 'DICT_SYSTEM_STATUS', '已过期', 'Expired');
INSERT INTO `sys_dict` VALUES (51, 'DICT_ROLE_ADMIN', NULL, 1, 'DICT_ROLE', '管理员', 'Admin');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operation` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `request_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `request_params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `request_time` int(11) NULL DEFAULT NULL,
  `request_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `user_agent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log_error
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_error`;
CREATE TABLE `sys_log_error`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `error_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `request_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `request_params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `request_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_agent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 57 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_date` datetime(0) NULL DEFAULT NULL,
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pid` int(11) NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `queue` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (100, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'setting', 'DICT_MENU_SYSTEM', NULL, '/main/system', 10);
INSERT INTO `sys_menu` VALUES (101, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'user', 'DICT_MENU_USER', 100, '/main/system/user', 1);
INSERT INTO `sys_menu` VALUES (102, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'team', 'DICT_MENU_ROLE', 100, '/main/system/role', 2);
INSERT INTO `sys_menu` VALUES (103, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'safety', 'DICT_MENU_AUTH', 100, '/main/system/auth', 3);
INSERT INTO `sys_menu` VALUES (104, '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'bars', 'DICT_MENU_MENU', 100, '/main/system/menu', 4);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_date` datetime(0) NULL DEFAULT NULL,
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  `dict_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('ADMIN', '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 0, 'DICT_ROLE_ADMIN');

-- ----------------------------
-- Table structure for sys_role_authorities
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_authorities`;
CREATE TABLE `sys_role_authorities`  (
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `authorities_id` int(11) NOT NULL,
  INDEX `FK9oc88v6ini6fjy2spcb8d63no`(`authorities_id`) USING BTREE,
  INDEX `FKonr80vngoq8033ca5xvwrm7wk`(`role_name`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_authorities
-- ----------------------------
INSERT INTO `sys_role_authorities` VALUES ('ADMIN', 1);
INSERT INTO `sys_role_authorities` VALUES ('ADMIN', 6);
INSERT INTO `sys_role_authorities` VALUES ('ADMIN', 11);
INSERT INTO `sys_role_authorities` VALUES ('ADMIN', 16);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `menu_id` int(11) NOT NULL,
  PRIMARY KEY (`menu_id`, `role_name`) USING BTREE,
  INDEX `FK5mnahfmtgnu7rh6uyxhn6unj1`(`role_name`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('ADMIN', 100);
INSERT INTO `sys_role_menu` VALUES ('ADMIN', 101);
INSERT INTO `sys_role_menu` VALUES ('ADMIN', 102);
INSERT INTO `sys_role_menu` VALUES ('ADMIN', 103);
INSERT INTO `sys_role_menu` VALUES ('ADMIN', 104);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created_date` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_date` datetime(0) NULL DEFAULT NULL,
  `modifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `version` int(11) NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `display_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gender` int(11) NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('administrator', '1990-01-01 00:00:00', 'build-in', '1990-01-01 00:00:00', 'build-in', 1, 1, NULL, '冯超', '13861800672@163.com', 1, '153D0B5569F101EF6A1AE8DBF6784F0D');

-- ----------------------------
-- Table structure for sys_user_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_roles`;
CREATE TABLE `sys_user_roles`  (
  `user_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `roles_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  INDEX `FKdyvsjmleoty67mnsu5tx4tw1a`(`roles_name`) USING BTREE,
  INDEX `FKeri4sltpx83c2lxq267357rj0`(`user_username`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_roles
-- ----------------------------
INSERT INTO `sys_user_roles` VALUES ('administrator', 'ADMIN');

SET FOREIGN_KEY_CHECKS = 1;
