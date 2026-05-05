package com.dorm.manage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BedAssignLog {
    private Long id;
    private Long studentId;
    private Long oldBedId;
    private Long newBedId;
    private Long assignUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime assignTime;
    private String remark;
}
