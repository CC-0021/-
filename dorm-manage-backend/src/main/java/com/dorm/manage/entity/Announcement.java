package com.dorm.manage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 公告表实体
 *
 * @author dorm-manage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    private Long id;
    private String title;
    private String content;
    private Long publisherId;
    private String publisherName;
    private LocalDateTime publishTime;
    /** 状态：0草稿 1已发布 2已撤回 */
    private Integer status;
    /** 是否置顶：0否 1是 */
    private Integer isTop;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
