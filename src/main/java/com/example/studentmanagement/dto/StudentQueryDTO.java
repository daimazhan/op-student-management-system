package com.example.studentmanagement.dto;

import lombok.Data;

@Data
public class StudentQueryDTO {
    private String keyword; // 模糊搜索关键词
    private String studentNo;
    private String name;
    private String gender;
    private String className;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
