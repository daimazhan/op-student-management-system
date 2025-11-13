package com.example.studentmanagement.mapper;

import com.example.studentmanagement.dto.OperationLogQueryDTO;
import com.example.studentmanagement.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OperationLogMapper {
    void insert(OperationLog operationLog);
    OperationLog findById(@Param("id") Long id);
    List<OperationLog> list(@Param("query") OperationLogQueryDTO queryDTO, 
                           @Param("offset") Integer offset, 
                           @Param("limit") Integer limit);
    Long count(@Param("query") OperationLogQueryDTO queryDTO);
    
    // 统计方法
    List<Map<String, Object>> countByType();  // 按操作类型统计
    List<Map<String, Object>> countByModule(); // 按操作模块统计
    List<Map<String, Object>> countByUser();   // 按用户统计
    List<Map<String, Object>> countByDate();   // 按日期统计
}

