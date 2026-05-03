package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 床位表实体
 *
 * @author dorm-manage
 */
@Data
public class Bed {
    private Long id;
    private Long dormitoryId;
    private String bedNo;
    /** 状态：0空闲 1已入住 2维修中 */
    private Integer status;
    private String studentId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /** 关联宿舍信息 */
    private Dormitory dormitory;
}
