package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 来访登记表实体
 *
 * @author dorm-manage
 */
@Data
public class Visitor {
    private Long id;
    private String visitorName;
    private String idCard;
    private String phone;
    private Long studentId;
    private String roomNo;
    private LocalDateTime enterTime;
    private LocalDateTime leaveTime;
    private Long registrarId;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
