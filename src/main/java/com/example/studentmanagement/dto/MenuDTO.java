package com.example.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MenuDTO {
    private Long id;
    
    @NotBlank(message = "菜单名称不能为空")
    private String name;
    
    private String path;
    private String component;
    private String icon;
    private Long parentId;
    private Integer sort;
    
    @NotBlank(message = "菜单类型不能为空")
    private Integer type;          // 0-目录，1-菜单，2-按钮
    
    private String permission;
    private Integer status = 1;    // 默认启用
}
