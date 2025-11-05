package com.example.studentmanagement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role; // ADMIN, USER
    private String realName; // 真实姓名
    private String email; // 邮箱
    private String phone; // 手机号
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
