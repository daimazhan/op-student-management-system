package com.example.studentmanagement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClassEntity {
    private Long id;
    private String className;
    private String grade;
    private String major;
    private String teacherName;
    private Integer studentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
