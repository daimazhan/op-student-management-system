package com.example.studentmanagement.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Menu {
    private Long id;
    private String name;           // 菜单名称
    private String path;           // 路由路径
    private String component;      // 组件路径
    private String icon;           // 图标
    private Long parentId;         // 父菜单ID
    private Integer sort;          // 排序
    private Integer type;          // 类型：0-目录，1-菜单，2-按钮
    private String permission;     // 权限标识
    private Integer status;        // 状态：0-禁用，1-启用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 子菜单列表
    private List<Menu> children;
}
