package com.dorm.manage.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色表实体
 *
 * @author dorm-manage
 */
@Data
public class Role {
    private Long id;
    private String roleName;
    private String roleCode;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /** 权限列表 */
    private List<Permission> permissions;
}
