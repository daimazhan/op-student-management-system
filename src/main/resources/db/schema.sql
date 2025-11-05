/*
 Navicat Premium Dump SQL

 Source Server         : macstudio
 Source Server Type    : MySQL
 Source Server Version : 90400 (9.4.0)
 Source Host           : localhost:3306
 Source Schema         : student_db

 Target Server Type    : MySQL
 Target Server Version : 90400 (9.4.0)
 File Encoding         : 65001

 Date: 05/11/2025 11:02:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `class_name` varchar(100) NOT NULL COMMENT '班级名称',
  `grade` varchar(20) DEFAULT NULL COMMENT '年级',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `teacher_name` varchar(50) DEFAULT NULL COMMENT '班主任姓名',
  `student_count` int DEFAULT '0' COMMENT '学生人数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `class_name` (`class_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班级表';

-- ----------------------------
-- Records of class
-- ----------------------------
BEGIN;
INSERT INTO `class` (`id`, `class_name`, `grade`, `major`, `teacher_name`, `student_count`, `create_time`, `update_time`) VALUES (1, '计算机1班', '2023级', '计算机科学与技术', '张老师', 0, '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `class` (`id`, `class_name`, `grade`, `major`, `teacher_name`, `student_count`, `create_time`, `update_time`) VALUES (2, '计算机2班', '2023级', '计算机科学与技术', '李老师', 0, '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `class` (`id`, `class_name`, `grade`, `major`, `teacher_name`, `student_count`, `create_time`, `update_time`) VALUES (3, '软件工程1班', '2023级', '软件工程', '王老师', 0, '2025-11-04 15:59:47', '2025-11-04 15:59:47');
COMMIT;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '菜单名称',
  `path` varchar(200) DEFAULT NULL COMMENT '路由路径',
  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID，0表示顶级菜单',
  `sort` int DEFAULT '0' COMMENT '排序',
  `type` int NOT NULL DEFAULT '1' COMMENT '类型：0-目录，1-菜单，2-按钮',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `status` int DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

-- ----------------------------
-- Records of menu
-- ----------------------------
BEGIN;
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (1, '首页', '/dashboard', 'views/dashboard/Dashboard', 'HomeFilled', 0, 1, 1, 'dashboard:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (2, '学生管理', '/student', 'views/student/Student', 'User', 0, 2, 1, 'student:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (3, '班级管理', '/class', 'views/class/Class', 'School', 0, 3, 1, 'class:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (4, '系统管理', '/system', '', 'Setting', 0, 99, 0, 'system:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (5, '菜单管理', '/system/menu', 'views/system/Menu', 'Menu', 4, 1, 1, 'menu:view', 1, '2025-11-04 16:30:49', '2025-11-04 16:30:49');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (6, '新增菜单', NULL, NULL, NULL, 5, 1, 2, 'menu:add', 1, '2025-11-04 22:22:24', '2025-11-04 22:30:24');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (7, '删除菜单', '', '', '', 5, 2, 2, 'menu:delete', 1, '2025-11-04 22:26:46', '2025-11-04 22:26:46');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (8, '修改菜单', NULL, NULL, NULL, 5, 3, 2, 'menu:edit', 1, '2025-11-04 22:22:24', '2025-11-04 22:30:47');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (10, '用户管理', '/system/user', 'views/system/User', 'User', 4, 2, 1, 'user:view', 1, '2025-11-04 22:52:17', '2025-11-04 22:52:17');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (11, '角色管理', '/system/role', 'views/system/Role', 'Lock', 4, 3, 1, 'role:view', 1, '2025-11-04 23:57:03', '2025-11-04 23:57:03');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (12, '编辑用户', '', '', '', 10, 0, 2, 'user:edit', 1, '2025-11-05 00:29:12', '2025-11-05 00:29:12');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (13, '新增用户', '', '', '', 10, 1, 2, 'user:add', 1, '2025-11-05 00:29:51', '2025-11-05 00:29:51');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (14, '分配权限', '', '', '', 11, 0, 2, 'role:assign', 1, '2025-11-05 00:31:11', '2025-11-05 00:33:46');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (15, '新增学生', '', '', '', 2, 1, 2, 'student:add', 1, '2025-11-05 09:38:29', '2025-11-05 09:38:29');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (16, '删除学生', '', '', '', 2, 2, 2, 'student:delete', 1, '2025-11-05 09:39:04', '2025-11-05 09:39:04');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (17, '修改学生', '', '', '', 2, 3, 2, 'student:edit', 1, '2025-11-05 09:39:50', '2025-11-05 09:40:03');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (18, '新增班级', '', '', '', 3, 1, 2, 'class:add', 1, '2025-11-05 09:40:55', '2025-11-05 09:41:19');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (19, '删除班级', '', '', '', 3, 2, 2, 'class:delete', 1, '2025-11-05 09:41:43', '2025-11-05 09:41:43');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (20, '修改班级', '', '', '', 3, 3, 2, 'class:edit', 1, '2025-11-05 09:42:13', '2025-11-05 09:42:13');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (21, '删除用户', '', '', '', 10, 3, 2, 'user:delete', 1, '2025-11-05 09:43:14', '2025-11-05 09:43:14');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (22, '新增角色', '', '', '', 11, 1, 2, 'role:add', 1, '2025-11-05 09:45:29', '2025-11-05 09:45:29');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (23, '删除角色', '', '', '', 11, 2, 2, 'role:delete', 1, '2025-11-05 09:46:03', '2025-11-05 09:46:03');
INSERT INTO `menu` (`id`, `name`, `path`, `component`, `icon`, `parent_id`, `sort`, `type`, `permission`, `status`, `create_time`, `update_time`) VALUES (24, '修改角色', '', '', '', 11, 3, 2, 'role:edit', 1, '2025-11-05 09:46:32', '2025-11-05 09:46:39');
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `status` int DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` (`id`, `role_code`, `role_name`, `description`, `status`, `create_time`, `update_time`) VALUES (1, 'ADMIN', '管理员', '系统管理员，拥有所有权限', 1, '2025-11-04 23:30:29', '2025-11-04 23:30:29');
INSERT INTO `role` (`id`, `role_code`, `role_name`, `description`, `status`, `create_time`, `update_time`) VALUES (2, 'USER', '普通用户', '普通用户，拥有基础权限', 1, '2025-11-04 23:30:29', '2025-11-04 23:30:29');
COMMIT;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`),
  KEY `idx_role` (`role_id`),
  KEY `idx_menu_id` (`menu_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of role_menu
-- ----------------------------
BEGIN;
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (51, 1, 1);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (52, 1, 2);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (56, 1, 3);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (60, 1, 4);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (61, 1, 5);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (62, 1, 6);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (63, 1, 7);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (64, 1, 8);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (65, 1, 10);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (69, 1, 11);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (66, 1, 12);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (67, 1, 13);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (70, 1, 14);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (53, 1, 15);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (54, 1, 16);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (55, 1, 17);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (57, 1, 18);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (58, 1, 19);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (59, 1, 20);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (68, 1, 21);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (71, 1, 22);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (72, 1, 23);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (73, 1, 24);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (8, 2, 1);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (9, 2, 2);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`) VALUES (10, 2, 3);
COMMIT;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_no` varchar(50) NOT NULL COMMENT '学号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `age` int DEFAULT NULL COMMENT '年龄',
  `class_name` varchar(100) DEFAULT NULL COMMENT '班级名称',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `student_no` (`student_no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生表';

-- ----------------------------
-- Records of student
-- ----------------------------
BEGIN;
INSERT INTO `student` (`id`, `student_no`, `name`, `gender`, `age`, `class_name`, `phone`, `email`, `address`, `create_time`, `update_time`) VALUES (1, '2023001', '张三', '男', 20, '计算机1班', '13800138001', 'zhangsan@example.com', '北京市朝阳区', '2025-11-04 15:59:47', '2025-11-05 00:02:21');
INSERT INTO `student` (`id`, `student_no`, `name`, `gender`, `age`, `class_name`, `phone`, `email`, `address`, `create_time`, `update_time`) VALUES (2, '2023002', '李四', '女', 19, '计算机1班', '13800138002', 'lisi@example.com', '上海市浦东新区', '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `student` (`id`, `student_no`, `name`, `gender`, `age`, `class_name`, `phone`, `email`, `address`, `create_time`, `update_time`) VALUES (3, '2023003', '王五', '男', 21, '计算机2班', '13800138003', 'wangwu@example.com', '广州市天河区', '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `student` (`id`, `student_no`, `name`, `gender`, `age`, `class_name`, `phone`, `email`, `address`, `create_time`, `update_time`) VALUES (4, '2023004', '赵六', '女', 20, '软件工程1班', '13800138004', 'zhaoliu@example.com', '深圳市南山区', '2025-11-04 15:59:47', '2025-11-04 15:59:47');
INSERT INTO `student` (`id`, `student_no`, `name`, `gender`, `age`, `class_name`, `phone`, `email`, `address`, `create_time`, `update_time`) VALUES (5, '2023005', '朱八', '男', 22, '软件工程1班', '', '', '', '2025-11-04 16:14:37', '2025-11-04 16:14:37');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN, USER',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `role`, `create_time`, `update_time`, `real_name`, `email`, `phone`) VALUES (1, 'admin', '0192023a7bbd73250516f069df18b500', 'ADMIN', '2025-11-04 15:59:47', '2025-11-05 10:14:30', '张三', '123@qq.com', '13988888888');
INSERT INTO `user` (`id`, `username`, `password`, `role`, `create_time`, `update_time`, `real_name`, `email`, `phone`) VALUES (2, 'user', '6ad14ba9986e3615423dfca256d04e3f', 'USER', '2025-11-04 15:59:47', '2025-11-05 10:00:25', '李四', '222@qq.com', '13699999999');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
