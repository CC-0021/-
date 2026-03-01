package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 留言表实体
 *
 * @author dorm-manage
 */
@Data
public class Message {
    private Long id;
    private Long studentId;
    private String title;
    private String content;
    private String phone;
    /** 状态：0未回复 1已回复 */
    private Integer status;
    private String replyContent;
    private Long replyUserId;
    private LocalDateTime replyTime;
    /** 学生是否已读 */
    private Integer isRead;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
