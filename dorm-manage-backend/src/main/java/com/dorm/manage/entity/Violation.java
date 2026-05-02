package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 违规记录表实体
 *
 * @author dorm-manage
 */
@Data
public class Violation {
    private Long id;
    private String studentId;
    private String studentName;
    private String roomNo;
    private String violationType;
    private LocalDateTime violationTime;
    private String description;
    private String evidence;
    private String handleResult;
    private Long handlerId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
