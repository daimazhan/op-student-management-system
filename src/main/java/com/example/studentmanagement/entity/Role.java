package com.example.studentmanagement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Role {
    private Long id;
    private String roleCode;       // 角色编码：ADMIN, USER 等
    private String roleName;       // 角色名称
    private String description;    // 角色描述
    private Integer status;        // 状态：0-禁用，1-启用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
