package com.example.studentmanagement.vo;

import lombok.Data;
import java.util.List;

@Data
public class MenuVO {
    private Long id;
    private String name;
    private String path;
    private String component;
    private String icon;
    private Long parentId;
    private Integer sort;
    private Integer type;
    private String permission;
    private List<MenuVO> children;
}
