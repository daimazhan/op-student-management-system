package com.example.studentmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.example.studentmanagement.annotation.LogOperation;
import com.example.studentmanagement.dto.OperationLogQueryDTO;
import com.example.studentmanagement.entity.OperationLog;
import com.example.studentmanagement.enums.OperationType;
import com.example.studentmanagement.service.OperationLogService;
import com.example.studentmanagement.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogService operationLogService;

    /**
     * 分页查询日志列表
     */
    @GetMapping("/list")
    @SaCheckPermission("log:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "日志管理", operationContent = "查询日志列表")
    public Result<Map<String, Object>> list(OperationLogQueryDTO queryDTO) {
        Map<String, Object> result = operationLogService.list(queryDTO);
        return Result.success(result);
    }

    /**
     * 根据ID查询日志详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("log:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "日志管理", operationContent = "查询日志详情")
    public Result<OperationLog> getById(@PathVariable Long id) {
        OperationLog log = operationLogService.getById(id);
        return Result.success(log);
    }

    /**
     * 保存操作日志（供其他服务调用，不需要权限验证）
     */
    @PostMapping
    public Result<?> save(@RequestBody OperationLog operationLog) {
        operationLogService.save(operationLog);
        return Result.success("保存成功");
    }

    /**
     * 按操作类型统计
     */
    @GetMapping("/statistics/type")
    @SaCheckPermission("log:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "日志管理", operationContent = "查询操作类型统计")
    public Result<List<Map<String, Object>>> countByType() {
        List<Map<String, Object>> result = operationLogService.countByType();
        return Result.success(result);
    }

    /**
     * 按操作模块统计
     */
    @GetMapping("/statistics/module")
    @SaCheckPermission("log:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "日志管理", operationContent = "查询操作模块统计")
    public Result<List<Map<String, Object>>> countByModule() {
        List<Map<String, Object>> result = operationLogService.countByModule();
        return Result.success(result);
    }

    /**
     * 按用户统计
     */
    @GetMapping("/statistics/user")
    @SaCheckPermission("log:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "日志管理", operationContent = "查询用户操作统计")
    public Result<List<Map<String, Object>>> countByUser() {
        List<Map<String, Object>> result = operationLogService.countByUser();
        return Result.success(result);
    }

    /**
     * 按日期统计（最近7天）
     */
    @GetMapping("/statistics/date")
    @SaCheckPermission("log:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "日志管理", operationContent = "查询日期操作统计")
    public Result<List<Map<String, Object>>> countByDate() {
        List<Map<String, Object>> result = operationLogService.countByDate();
        return Result.success(result);
    }
}

