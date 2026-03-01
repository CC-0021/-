package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限表实体
 *
 * @author dorm-manage
 */
@Data
public class Permission {
    private Long id;
    private Long parentId;
    private String permissionName;
    private String permissionCode;
    /** 类型：1菜单 2按钮 */
    private Integer permissionType;
    private String path;
    private Integer sortOrder;
    private LocalDateTime createTime;
}
