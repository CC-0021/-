package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工信息表实体
 *
 * @author dorm-manage
 */
@Data
public class Employee {
    private Long id;
    private String name;
    private Integer gender;
    private String phone;
    private String department;
    private String position;
    private LocalDate hireDate;
    /** 状态：0离职 1在职 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
