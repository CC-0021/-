package com.dorm.manage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报修表实体
 *
 * @author dorm-manage
 */
@Data
public class Repair {
    private Long id;
    private String repairNo;
    private Long studentId;
    /** 报修类型 */
    private String repairType;
    private String roomNo;
    private String description;
    private String images;
    private String phone;
    /** 状态：0待处理 1处理中 2已完成 3已撤销 */
    private Integer status;
    private Long handlerId;
    private String handlerName;
    private String handleRemark;
    private String handleImages;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime completeTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updateTime;
}
