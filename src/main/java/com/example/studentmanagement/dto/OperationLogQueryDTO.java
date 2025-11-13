package com.example.studentmanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperationLogQueryDTO {
    private String username;          // 用户名
    private String operationType;     // 操作类型
    private String operationModule;   // 操作模块
    private Integer status;           // 状态
    private LocalDateTime startTime;  // 开始时间
    private LocalDateTime endTime;    // 结束时间
    private Integer pageNum = 1;      // 页码
    private Integer pageSize = 10;    // 每页数量
}

