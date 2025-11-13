package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.OperationLogQueryDTO;
import com.example.studentmanagement.entity.OperationLog;
import com.example.studentmanagement.mapper.OperationLogMapper;
import com.example.studentmanagement.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    @Override
    public void save(OperationLog operationLog) {
        if (operationLog.getOperationTime() == null) {
            operationLog.setOperationTime(LocalDateTime.now());
        }
        operationLogMapper.insert(operationLog);
    }

    @Override
    public OperationLog getById(Long id) {
        return operationLogMapper.findById(id);
    }

    @Override
    public Map<String, Object> list(OperationLogQueryDTO queryDTO) {
        // 如果没有设置分页参数，设置默认值
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
            queryDTO.setPageNum(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }

        int offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();
        List<OperationLog> logs = operationLogMapper.list(queryDTO, offset, queryDTO.getPageSize());
        Long total = operationLogMapper.count(queryDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("list", logs);
        result.put("total", total);
        result.put("pageNum", queryDTO.getPageNum());
        result.put("pageSize", queryDTO.getPageSize());
        return result;
    }

    @Override
    public List<Map<String, Object>> countByType() {
        return operationLogMapper.countByType();
    }

    @Override
    public List<Map<String, Object>> countByModule() {
        return operationLogMapper.countByModule();
    }

    @Override
    public List<Map<String, Object>> countByUser() {
        return operationLogMapper.countByUser();
    }

    @Override
    public List<Map<String, Object>> countByDate() {
        return operationLogMapper.countByDate();
    }
}

