package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宿舍表实体
 *
 * @author dorm-manage
 */
@Data
public class Dormitory {
    private Long id;
    private String buildingNo;
    private Integer floorNo;
    private String roomNo;
    private String dormType;
    private Integer bedTotal;
    private Integer bedAvailable;
    /** 状态：0维修中 1正常 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /** 床位列表 */
    private List<Bed> beds;
}
