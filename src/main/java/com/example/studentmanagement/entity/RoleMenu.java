package com.example.studentmanagement.entity;

import lombok.Data;

@Data
public class RoleMenu {
    private Long id;
    private Long roleId;           // 角色ID
    private Long menuId;           // 菜单ID
}
