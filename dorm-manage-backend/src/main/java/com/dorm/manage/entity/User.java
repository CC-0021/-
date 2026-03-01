package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表实体（学生+管理员）
 *
 * @author dorm-manage
 */
@Data
public class User {
    private Long id;
    /** 登录账号（学号/工号） */
    private String username;
    /** 密码（BCrypt加密） */
    private String password;
    private String realName;
    /** 用户类型：1学生 2管理员 */
    private Integer userType;
    private Integer gender;
    private String phone;
    private String idCard;
    private String college;
    private String major;
    private String className;
    /** 状态：0禁用 1正常 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
