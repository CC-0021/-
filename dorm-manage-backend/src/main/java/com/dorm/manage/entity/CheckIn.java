package com.dorm.manage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 入住登记表实体
 *
 * @author dorm-manage
 */
@Data
public class CheckIn {
    private Long id;
    private Long studentId;
    private String studentNo;
    private String realName;
    private Integer gender;
    private String college;
    private String major;
    private String className;
    private String idCard;
    private String phone;
    private Long dormitoryId;
    private String roomDisplay;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime checkInTime;
    /** 状态：0待审核 1审核通过 2审核驳回 */
    private Integer status;
    private String rejectReason;
    private Long auditUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime auditTime;
    private Long bedId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updateTime;
}
