/*
 Navicat Premium Dump SQL

 Source Server         : nas
 Source Server Type    : MySQL
 Source Server Version : 50709 (5.7.9)
 Source Host           : 192.168.1.25:3306
 Source Schema         : student_db

 Target Server Type    : MySQL
 Target Server Version : 50709 (5.7.9)
 File Encoding         : 65001

 Date: 13/11/2025 17:44:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班级名称',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年级',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班主任姓名',
  `student_count` int(11) NULL DEFAULT 0 COMMENT '学生人数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `class_name`(`class_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班级表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of class
-- ----------------------------
INSERT INTO `class` VALUES (1, '计算机1班', '2023级', '计算机科学与技术', '张老师', 0, '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `class` VALUES (2, '计算机2班', '2023级', '计算机科学与技术', '李老师', 0, '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `class` VALUES (3, '软件工程1班', '2023级', '软件工程', '王老师', 0, '2025-11-04 15:59:47', '2025-11-04 15:59:47');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由路径',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID，0表示顶级菜单',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `type` int(11) NOT NULL DEFAULT 1 COMMENT '类型：0-目录，1-菜单，2-按钮',
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '首页', '/dashboard', 'views/dashboard/Dashboard', 'HomeFilled', 0, 1, 1, 'dashboard:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` VALUES (2, '学生管理', '/student', 'views/student/Student', 'User', 0, 2, 1, 'student:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` VALUES (3, '班级管理', '/class', 'views/class/Class', 'School', 0, 3, 1, 'class:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` VALUES (4, '系统管理', '/system', '', 'Setting', 0, 99, 1, 'system:view', 1, '2025-11-04 16:30:49', '2025-11-13 16:54:13');
INSERT INTO `menu` VALUES (5, '菜单管理', '/system/menu', 'views/system/Menu', 'Menu', 4, 1, 1, 'menu:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` VALUES (6, '新增菜单', NULL, NULL, NULL, 5, 1, 2, 'menu:add', 1, '2025-11-04 22:22:24', '2025-11-04 22:30:24');
INSERT INTO `menu` VALUES (7, '删除菜单', '', '', '', 5, 2, 2, 'menu:delete', 1, '2025-11-04 22:26:46', '2025-11-04 22:26:46');
INSERT INTO `menu` VALUES (8, '修改菜单', NULL, NULL, NULL, 5, 3, 2, 'menu:edit', 1, '2025-11-04 22:22:24', '2025-11-04 22:30:47');
INSERT INTO `menu` VALUES (10, '用户管理', '/system/user', 'views/system/User', 'User', 4, 2, 1, 'user:view', 1, '2025-11-04 22:52:17', '2025-11-04 22:52:17');
INSERT INTO `menu` VALUES (11, '角色管理', '/system/role', 'views/system/Role', 'Lock', 4, 3, 1, 'role:view', 1, '2025-11-04 23:57:03', '2025-11-04 23:57:03');
INSERT INTO `menu` VALUES (12, '编辑用户', '', '', '', 10, 0, 2, 'user:edit', 1, '2025-11-05 00:29:12', '2025-11-05 00:29:12');
INSERT INTO `menu` VALUES (13, '新增用户', '', '', '', 10, 1, 2, 'user:add', 1, '2025-11-05 00:29:51', '2025-11-05 00:29:51');
INSERT INTO `menu` VALUES (14, '分配权限', '', '', '', 11, 0, 2, 'role:assign', 1, '2025-11-05 00:31:11', '2025-11-05 00:33:46');
INSERT INTO `menu` VALUES (15, '新增学生', '', '', '', 2, 1, 2, 'student:add', 1, '2025-11-05 09:38:29', '2025-11-05 09:38:29');
INSERT INTO `menu` VALUES (16, '删除学生', '', '', '', 2, 2, 2, 'student:delete', 1, '2025-11-05 09:39:04', '2025-11-05 09:39:04');
INSERT INTO `menu` VALUES (17, '修改学生', '', '', '', 2, 3, 2, 'student:edit', 1, '2025-11-05 09:39:50', '2025-11-05 09:40:03');
INSERT INTO `menu` VALUES (18, '新增班级', '', '', '', 3, 1, 2, 'class:add', 1, '2025-11-05 09:40:55', '2025-11-05 09:41:19');
INSERT INTO `menu` VALUES (19, '删除班级', '', '', '', 3, 2, 2, 'class:delete', 1, '2025-11-05 09:41:43', '2025-11-05 09:41:43');
INSERT INTO `menu` VALUES (20, '修改班级', '', '', '', 3, 3, 2, 'class:edit', 1, '2025-11-05 09:42:13', '2025-11-05 09:42:13');
INSERT INTO `menu` VALUES (21, '删除用户', '', '', '', 10, 3, 2, 'user:delete', 1, '2025-11-05 09:43:14', '2025-11-05 09:43:14');
INSERT INTO `menu` VALUES (22, '新增角色', '', '', '', 11, 1, 2, 'role:add', 1, '2025-11-05 09:45:29', '2025-11-05 09:45:29');
INSERT INTO `menu` VALUES (23, '删除角色', '', '', '', 11, 2, 2, 'role:delete', 1, '2025-11-05 09:46:03', '2025-11-05 09:46:03');
INSERT INTO `menu` VALUES (24, '修改角色', '', '', '', 11, 3, 2, 'role:edit', 1, '2025-11-05 09:46:32', '2025-11-05 09:46:39');
INSERT INTO `menu` VALUES (25, '日志管理', '/system/log', 'views/system/Log', 'Monitor', 4, 4, 1, 'log:view', 1, '2025-11-13 16:24:47', '2025-11-13 17:17:45');
INSERT INTO `menu` VALUES (26, '新增日志', '', '', '', 25, 0, 2, 'log:add', 1, '2025-11-13 16:29:17', '2025-11-13 16:29:17');
INSERT INTO `menu` VALUES (27, '修改日志', '', '', '', 25, 0, 2, 'log:edit', 1, '2025-11-13 16:29:45', '2025-11-13 16:29:45');
INSERT INTO `menu` VALUES (28, '删除日志', '', '', '', 25, 0, 2, 'log:delete', 1, '2025-11-13 16:30:04', '2025-11-13 16:30:04');

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型：ADD-新增, UPDATE-修改, DELETE-删除, QUERY-查询, LOGIN-登录, LOGOUT-登出',
  `operation_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作模块：学生管理、班级管理、用户管理等',
  `operation_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法：GET、POST、PUT、DELETE',
  `request_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URL',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态：0-失败，1-成功',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误信息',
  `operation_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_operation_type`(`operation_type`) USING BTREE,
  INDEX `idx_operation_module`(`operation_module`) USING BTREE,
  INDEX `idx_operation_time`(`operation_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of operation_log
-- ----------------------------
INSERT INTO `operation_log` VALUES (1, 1, 'admin', 'LOGOUT', '认证授权', '用户登出', 'POST', '/api/auth/logout', '0:0:0:0:0:0:0:1', 1, NULL, '2025-11-13 17:35:55');
INSERT INTO `operation_log` VALUES (2, 1, 'admin', 'LOGIN', '认证授权', '用户登录', 'POST', '/api/auth/login', '0:0:0:0:0:0:0:1', 1, NULL, '2025-11-13 17:36:01');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'ADMIN', '管理员', '系统管理员，拥有所有权限', 1, '2025-11-04 23:30:29', '2025-11-04 23:30:29');
INSERT INTO `role` VALUES (2, 'USER', '普通用户', '普通用户，拥有基础权限', 1, '2025-11-04 23:30:29', '2025-11-04 23:30:29');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_menu`(`role_id`, `menu_id`) USING BTREE,
  INDEX `idx_role`(`role_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 129 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (102, 1, 1);
INSERT INTO `role_menu` VALUES (103, 1, 2);
INSERT INTO `role_menu` VALUES (107, 1, 3);
INSERT INTO `role_menu` VALUES (111, 1, 4);
INSERT INTO `role_menu` VALUES (112, 1, 5);
INSERT INTO `role_menu` VALUES (113, 1, 6);
INSERT INTO `role_menu` VALUES (114, 1, 7);
INSERT INTO `role_menu` VALUES (115, 1, 8);
INSERT INTO `role_menu` VALUES (116, 1, 10);
INSERT INTO `role_menu` VALUES (120, 1, 11);
INSERT INTO `role_menu` VALUES (117, 1, 12);
INSERT INTO `role_menu` VALUES (118, 1, 13);
INSERT INTO `role_menu` VALUES (121, 1, 14);
INSERT INTO `role_menu` VALUES (104, 1, 15);
INSERT INTO `role_menu` VALUES (105, 1, 16);
INSERT INTO `role_menu` VALUES (106, 1, 17);
INSERT INTO `role_menu` VALUES (108, 1, 18);
INSERT INTO `role_menu` VALUES (109, 1, 19);
INSERT INTO `role_menu` VALUES (110, 1, 20);
INSERT INTO `role_menu` VALUES (119, 1, 21);
INSERT INTO `role_menu` VALUES (122, 1, 22);
INSERT INTO `role_menu` VALUES (123, 1, 23);
INSERT INTO `role_menu` VALUES (124, 1, 24);
INSERT INTO `role_menu` VALUES (125, 1, 25);
INSERT INTO `role_menu` VALUES (126, 1, 26);
INSERT INTO `role_menu` VALUES (127, 1, 27);
INSERT INTO `role_menu` VALUES (128, 1, 28);
INSERT INTO `role_menu` VALUES (8, 2, 1);
INSERT INTO `role_menu` VALUES (9, 2, 2);
INSERT INTO `role_menu` VALUES (10, 2, 3);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级名称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `student_no`(`student_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '2023001', '张三', '男', 20, '计算机1班', '13800138001', 'zhangsan@example.com', '北京市朝阳区', '2025-11-04 15:59:47', '2025-11-05 00:02:21');
INSERT INTO `student` VALUES (2, '2023002', '李四', '女', 19, '计算机1班', '13800138002', 'lisi@example.com', '上海市浦东新区', '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `student` VALUES (3, '2023003', '王五', '男', 21, '计算机2班', '13800138003', 'wangwu@example.com', '广州市天河区', '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `student` VALUES (4, '2023004', '赵六', '女', 20, '软件工程1班', '13800138004', 'zhaoliu@example.com', '深圳市南山区', '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `student` VALUES (5, '2023005', '朱八', '男', 22, '软件工程1班', '13699999999', '', '', '2025-11-04 16:14:37', '2025-11-13 17:19:27');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN, USER',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '0192023a7bbd73250516f069df18b500', 'ADMIN', '2025-11-04 15:59:47', '2025-11-05 10:14:30', '张三', '123@qq.com', '13988888888');
INSERT INTO `user` VALUES (2, 'user', '6ad14ba9986e3615423dfca256d04e3f', 'USER', '2025-11-04 15:59:47', '2025-11-05 10:00:25', '李四', '222@qq.com', '13699999999');

SET FOREIGN_KEY_CHECKS = 1;
