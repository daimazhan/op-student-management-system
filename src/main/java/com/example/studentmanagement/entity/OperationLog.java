package com.example.studentmanagement.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperationLog {
    private Long id;
    private Long userId;              // 用户ID
    private String username;          // 用户名
    private String operationType;     // 操作类型：ADD-新增, UPDATE-修改, DELETE-删除, QUERY-查询, LOGIN-登录, LOGOUT-登出
    private String operationModule;   // 操作模块：学生管理、班级管理、用户管理等
    private String operationContent;  // 操作内容
    private String requestMethod;     // 请求方法：GET、POST、PUT、DELETE
    private String requestUrl;        // 请求URL
    private String ipAddress;         // IP地址
    private Integer status;           // 状态：0-失败，1-成功
    private String errorMessage;      // 错误信息
    private LocalDateTime operationTime; // 操作时间
}

