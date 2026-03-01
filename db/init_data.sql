-- =============================================
-- 大学生公寓管理系统 - 初始化数据
-- =============================================

SET NAMES utf8mb4;

-- 密码均为 BCrypt 加密后的 123456
-- BCrypt hash for "123456": $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi

-- ----------------------------
-- 角色
-- ----------------------------
INSERT INTO `t_role` (`id`, `role_name`, `role_code`, `description`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有系统全部权限'),
(2, '普通管理员', 'ADMIN', '可分配菜单与按钮权限');

-- ----------------------------
-- 权限（菜单+按钮示例）
-- ----------------------------
INSERT INTO `t_permission` (`id`, `parent_id`, `permission_name`, `permission_code`, `permission_type`, `path`, `sort_order`) VALUES
(1, 0, '公告管理', 'menu:announcement', 1, '/announcement', 1),
(2, 1, '删除公告', 'btn:announcement:delete', 2, NULL, 0),
(3, 1, '发布公告', 'btn:announcement:publish', 2, NULL, 0),
(4, 0, '床位管理', 'menu:bed', 1, '/bed', 2),
(5, 0, '宿舍管理', 'menu:dormitory', 1, '/dormitory', 3),
(6, 0, '权限管理', 'menu:permission', 1, '/permission', 4),
(7, 0, '员工管理', 'menu:employee', 1, '/employee', 5),
(8, 0, '宿舍分配', 'menu:assign', 1, '/assign', 6),
(9, 0, '违规管理', 'menu:violation', 1, '/violation', 7),
(10, 0, '来访登记', 'menu:visitor', 1, '/visitor', 8),
(11, 0, '入住审核', 'menu:checkin', 1, '/checkin', 9),
(12, 0, '报修管理', 'menu:repair', 1, '/repair', 10),
(13, 0, '留言回复', 'menu:message', 1, '/message', 11),
(14, 0, '宿舍申请审核', 'menu:dormApplication', 1, '/dormApplication', 12),
(15, 0, '来访统计', 'menu:visitorStats', 1, '/visitorStats', 13);

-- ----------------------------
-- 超级管理员角色拥有全部权限
-- ----------------------------
INSERT INTO `t_role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM t_permission;

-- ----------------------------
-- 普通管理员角色（部分权限示例）
-- ----------------------------
INSERT INTO `t_role_permission` (`role_id`, `permission_id`) VALUES
(2, 1), (2, 3), (2, 4), (2, 5), (2, 7), (2, 8), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14), (2, 15);

-- ----------------------------
-- 管理员账号：admin / 123456
-- ----------------------------
INSERT INTO `t_user` (`id`, `username`, `password`, `real_name`, `user_type`, `gender`, `phone`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 2, 1, '13800138000', 1);

INSERT INTO `t_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- ----------------------------
-- 测试学生账号：2024001 / 123456
-- ----------------------------
INSERT INTO `t_user` (`id`, `username`, `password`, `real_name`, `user_type`, `gender`, `phone`, `college`, `major`, `class_name`, `status`) VALUES
(2, '2024001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三', 1, 1, '13900139001', '计算机学院', '软件工程', '软工2401', 1);

-- ----------------------------
-- 示例宿舍与床位
-- ----------------------------
INSERT INTO `t_dormitory` (`id`, `building_no`, `floor_no`, `room_no`, `dorm_type`, `bed_total`, `bed_available`, `status`) VALUES
(1, 'A栋', 1, '101', '4人间', 4, 4, 1),
(2, 'A栋', 1, '102', '4人间', 4, 4, 1),
(3, 'A栋', 2, '201', '6人间', 6, 6, 1);

INSERT INTO `t_bed` (`dormitory_id`, `bed_no`, `status`) VALUES
(1, '1', 0), (1, '2', 0), (1, '3', 0), (1, '4', 0),
(2, '1', 0), (2, '2', 0), (2, '3', 0), (2, '4', 0),
(3, '1', 0), (3, '2', 0), (3, '3', 0), (3, '4', 0), (3, '5', 0), (3, '6', 0);

-- ----------------------------
-- 示例公告
-- ----------------------------
INSERT INTO `t_announcement` (`title`, `content`, `publisher_id`, `publisher_name`, `publish_time`, `status`, `is_top`) VALUES
('欢迎入住学生公寓', '请同学们遵守宿舍管理规定，注意用电安全。', 1, '系统管理员', NOW(), 1, 1),
('清明节假期宿舍安排通知', '假期期间请锁好门窗，贵重物品妥善保管。', 1, '系统管理员', NOW(), 1, 0);
