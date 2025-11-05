package com.example.studentmanagement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Student {
    private Long id;
    private String studentNo;
    private String name;
    private String gender;
    private Integer age;
    private String className;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
