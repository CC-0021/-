package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报修评价表实体
 *
 * @author dorm-manage
 */
@Data
public class RepairEvaluation {
    private Long id;
    private Long repairId;
    private Long studentId;
    /** 星级1-5 */
    private Integer score;
    private String content;
    private LocalDateTime createTime;
}
