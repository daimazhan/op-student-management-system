package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.OperationLogQueryDTO;
import com.example.studentmanagement.entity.OperationLog;

import java.util.List;
import java.util.Map;

public interface OperationLogService {
    /**
     * 保存操作日志
     */
    void save(OperationLog operationLog);

    /**
     * 根据ID查询日志
     */
    OperationLog getById(Long id);

    /**
     * 分页查询日志列表
     */
    Map<String, Object> list(OperationLogQueryDTO queryDTO);

    /**
     * 按操作类型统计
     */
    List<Map<String, Object>> countByType();

    /**
     * 按操作模块统计
     */
    List<Map<String, Object>> countByModule();

    /**
     * 按用户统计
     */
    List<Map<String, Object>> countByUser();

    /**
     * 按日期统计（最近7天）
     */
    List<Map<String, Object>> countByDate();
}

