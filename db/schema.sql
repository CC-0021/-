-- =============================================
-- 大学生公寓管理系统 - 数据库建表脚本
-- MySQL 8.0, 字符集 utf8mb4
-- =============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 用户表（学生+管理员）
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) NOT NULL COMMENT '登录账号（学号/工号）',
  `password` varchar(128) NOT NULL COMMENT '密码（BCrypt加密）',
  `real_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `user_type` tinyint NOT NULL DEFAULT '1' COMMENT '用户类型：1学生 2管理员',
  `gender` tinyint DEFAULT NULL COMMENT '性别：0未知 1男 2女',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `college` varchar(64) DEFAULT NULL COMMENT '学院（学生）',
  `major` varchar(64) DEFAULT NULL COMMENT '专业（学生）',
  `class_name` varchar(32) DEFAULT NULL COMMENT '班级（学生）',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0禁用 1正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(32) NOT NULL COMMENT '角色名称',
  `role_code` varchar(32) NOT NULL COMMENT '角色编码',
  `description` varchar(128) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ----------------------------
-- 权限表
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父级ID',
  `permission_name` varchar(64) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(64) NOT NULL COMMENT '权限编码（如menu:announcement）',
  `permission_type` tinyint NOT NULL DEFAULT '1' COMMENT '类型：1菜单 2按钮',
  `path` varchar(128) DEFAULT NULL COMMENT '路由路径',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- ----------------------------
-- 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ----------------------------
-- 角色权限关联表
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ----------------------------
-- 公告表
-- ----------------------------
DROP TABLE IF EXISTS `t_announcement`;
CREATE TABLE `t_announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(128) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `publisher_id` bigint DEFAULT NULL COMMENT '发布人ID',
  `publisher_name` varchar(32) DEFAULT NULL COMMENT '发布人姓名',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0草稿 1已发布 2已撤回',
  `is_top` tinyint NOT NULL DEFAULT '0' COMMENT '是否置顶：0否 1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_is_top` (`is_top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- ----------------------------
-- 宿舍表
-- ----------------------------
DROP TABLE IF EXISTS `t_dormitory`;
CREATE TABLE `t_dormitory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_no` varchar(16) NOT NULL COMMENT '楼栋号',
  `floor_no` int NOT NULL COMMENT '楼层',
  `room_no` varchar(16) NOT NULL COMMENT '房间号',
  `dorm_type` varchar(16) NOT NULL DEFAULT '4人间' COMMENT '宿舍类型：4人间/6人间',
  `bed_total` int NOT NULL DEFAULT '4' COMMENT '床位总数',
  `bed_available` int NOT NULL DEFAULT '4' COMMENT '空闲床位数',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0维修中 1正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_floor_room` (`building_no`,`floor_no`,`room_no`),
  KEY `idx_building_no` (`building_no`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍表';

-- ----------------------------
-- 床位表
-- ----------------------------
DROP TABLE IF EXISTS `t_bed`;
CREATE TABLE `t_bed` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dormitory_id` bigint NOT NULL COMMENT '宿舍ID',
  `bed_no` varchar(8) NOT NULL COMMENT '床位号',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0空闲 1已入住 2维修中',
  `student_id` bigint DEFAULT NULL COMMENT '入住学生ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dorm_bed` (`dormitory_id`,`bed_no`),
  KEY `idx_status` (`status`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='床位表';

-- ----------------------------
-- 入住登记表
-- ----------------------------
DROP TABLE IF EXISTS `t_check_in`;
CREATE TABLE `t_check_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `student_no` varchar(32) NOT NULL COMMENT '学号',
  `real_name` varchar(32) NOT NULL COMMENT '姓名',
  `gender` tinyint DEFAULT NULL COMMENT '性别',
  `college` varchar(64) DEFAULT NULL COMMENT '学院',
  `major` varchar(64) DEFAULT NULL COMMENT '专业',
  `class_name` varchar(32) DEFAULT NULL COMMENT '班级',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `dormitory_id` bigint DEFAULT NULL COMMENT '拟入住宿舍ID（只读）',
  `room_display` varchar(32) DEFAULT NULL COMMENT '拟入住楼栋/房间展示',
  `check_in_time` datetime DEFAULT NULL COMMENT '拟入住时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0待审核 1审核通过 2审核驳回',
  `reject_reason` varchar(256) DEFAULT NULL COMMENT '驳回理由',
  `audit_user_id` bigint DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `bed_id` bigint DEFAULT NULL COMMENT '分配床位ID（审核通过后）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入住登记表';

-- ----------------------------
-- 报修表
-- ----------------------------
DROP TABLE IF EXISTS `t_repair`;
CREATE TABLE `t_repair` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `repair_no` varchar(32) NOT NULL COMMENT '报修单号',
  `student_id` bigint NOT NULL COMMENT '报修学生ID',
  `repair_type` varchar(32) NOT NULL COMMENT '报修类型：水电故障/家具损坏/网络问题/卫生问题/其他',
  `room_no` varchar(32) NOT NULL COMMENT '宿舍号',
  `description` text NOT NULL COMMENT '问题描述',
  `images` varchar(512) DEFAULT NULL COMMENT '图片URL，多张逗号分隔',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0待处理 1处理中 2已完成 3已撤销',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(32) DEFAULT NULL COMMENT '处理人姓名',
  `handle_remark` varchar(512) DEFAULT NULL COMMENT '处理备注',
  `handle_images` varchar(512) DEFAULT NULL COMMENT '处理凭证图片',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_repair_no` (`repair_no`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_status` (`status`),
  KEY `idx_repair_type` (`repair_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_handler_id` (`handler_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修表';

-- ----------------------------
-- 报修评价表
-- ----------------------------
DROP TABLE IF EXISTS `t_repair_evaluation`;
CREATE TABLE `t_repair_evaluation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `repair_id` bigint NOT NULL COMMENT '报修单ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `score` tinyint NOT NULL COMMENT '星级评分1-5',
  `content` varchar(512) DEFAULT NULL COMMENT '评价内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_repair_id` (`repair_id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修评价表';

-- ----------------------------
-- 宿舍申请表
-- ----------------------------
DROP TABLE IF EXISTS `t_dorm_application`;
CREATE TABLE `t_dorm_application` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `application_type` varchar(16) NOT NULL COMMENT '申请类型：换宿舍/入住/退宿',
  `original_room` varchar(32) DEFAULT NULL COMMENT '原宿舍号',
  `target_room` varchar(32) DEFAULT NULL COMMENT '目标宿舍号（换宿）',
  `reason` varchar(512) NOT NULL COMMENT '申请理由（≥20字）',
  `attachment` varchar(256) DEFAULT NULL COMMENT '附件URL',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0待审核 1审核通过 2审核驳回',
  `reject_reason` varchar(256) DEFAULT NULL COMMENT '驳回理由',
  `audit_user_id` bigint DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_status` (`status`),
  KEY `idx_application_type` (`application_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍申请表';

-- ----------------------------
-- 来访登记表
-- ----------------------------
DROP TABLE IF EXISTS `t_visitor`;
CREATE TABLE `t_visitor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `visitor_name` varchar(32) NOT NULL COMMENT '访客姓名',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `student_id` bigint DEFAULT NULL COMMENT '被访学生ID',
  `room_no` varchar(32) DEFAULT NULL COMMENT '来访宿舍号',
  `enter_time` datetime DEFAULT NULL COMMENT '进入时间',
  `leave_time` datetime DEFAULT NULL COMMENT '离开时间',
  `registrar_id` bigint DEFAULT NULL COMMENT '登记人ID',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_enter_time` (`enter_time`),
  KEY `idx_room_no` (`room_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='来访登记表';

-- ----------------------------
-- 违规记录表
-- ----------------------------
DROP TABLE IF EXISTS `t_violation`;
CREATE TABLE `t_violation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `room_no` varchar(32) NOT NULL COMMENT '宿舍号',
  `violation_type` varchar(32) NOT NULL COMMENT '违规类型：晚归/使用违规电器/卫生不合格等',
  `violation_time` datetime NOT NULL COMMENT '违规时间',
  `description` varchar(512) DEFAULT NULL COMMENT '违规描述',
  `evidence` varchar(512) DEFAULT NULL COMMENT '凭证（图片/视频）',
  `handle_result` varchar(256) DEFAULT NULL COMMENT '处理结果',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_violation_time` (`violation_time`),
  KEY `idx_room_no` (`room_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='违规记录表';

-- ----------------------------
-- 员工信息表
-- ----------------------------
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `gender` tinyint DEFAULT NULL COMMENT '性别：1男 2女',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `department` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `position` varchar(32) DEFAULT NULL COMMENT '职位',
  `hire_date` date DEFAULT NULL COMMENT '入职时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0离职 1在职',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_department` (`department`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工信息表';

-- ----------------------------
-- 留言表
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `title` varchar(128) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0未回复 1已回复',
  `reply_content` text COMMENT '回复内容',
  `reply_user_id` bigint DEFAULT NULL COMMENT '回复人ID',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '学生是否已读：0未读 1已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留言表';

-- ----------------------------
-- 公告查看记录表（可选）
-- ----------------------------
DROP TABLE IF EXISTS `t_announcement_read`;
CREATE TABLE `t_announcement_read` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `announcement_id` bigint NOT NULL COMMENT '公告ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '查看时间',
  PRIMARY KEY (`id`),
  KEY `idx_announcement_id` (`announcement_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告查看记录表';

-- ----------------------------
-- 宿舍分配记录表
-- ----------------------------
DROP TABLE IF EXISTS `t_bed_assign_log`;
CREATE TABLE `t_bed_assign_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `old_bed_id` bigint DEFAULT NULL COMMENT '原床位ID',
  `new_bed_id` bigint NOT NULL COMMENT '新床位ID',
  `assign_user_id` bigint NOT NULL COMMENT '分配人ID',
  `assign_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_assign_time` (`assign_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍分配记录表';

SET FOREIGN_KEY_CHECKS = 1;
