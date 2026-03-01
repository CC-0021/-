package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宿舍申请表实体
 *
 * @author dorm-manage
 */
@Data
public class DormApplication {
    private Long id;
    private Long studentId;
    /** 申请类型：换宿舍/入住/退宿 */
    private String applicationType;
    private String originalRoom;
    private String targetRoom;
    private String reason;
    private String attachment;
    /** 状态：0待审核 1审核通过 2审核驳回 */
    private Integer status;
    private String rejectReason;
    private Long auditUserId;
    private LocalDateTime auditTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
