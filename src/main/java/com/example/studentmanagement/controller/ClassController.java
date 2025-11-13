package com.example.studentmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.example.studentmanagement.annotation.LogOperation;
import com.example.studentmanagement.entity.ClassEntity;
import com.example.studentmanagement.enums.OperationType;
import com.example.studentmanagement.service.ClassService;
import com.example.studentmanagement.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    /**
     * 获取班级列表
     */
    @GetMapping("/list")
    @SaCheckPermission("class:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "班级管理", operationContent = "查询班级列表")
    public Result<List<ClassEntity>> list() {
        List<ClassEntity> list = classService.list();
        return Result.success(list);
    }

    /**
     * 获取班级详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("class:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "班级管理", operationContent = "查询班级详情")
    public Result<ClassEntity> getById(@PathVariable Long id) {
        ClassEntity classEntity = classService.getById(id);
        return Result.success(classEntity);
    }

    /**
     * 新增班级
     */
    @PostMapping
    @SaCheckPermission("class:add")
    @LogOperation(operationType = OperationType.ADD, operationModule = "班级管理", operationContent = "新增班级")
    public Result<?> save(@Valid @RequestBody ClassEntity classEntity) {
        classService.save(classEntity);
        return Result.success("保存成功");
    }

    /**
     * 更新班级
     */
    @PutMapping
    @SaCheckPermission("class:edit")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "班级管理", operationContent = "更新班级")
    public Result<?> update(@Valid @RequestBody ClassEntity classEntity) {
        classService.update(classEntity);
        return Result.success("更新成功");
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("class:delete")
    @LogOperation(operationType = OperationType.DELETE, operationModule = "班级管理", operationContent = "删除班级")
    public Result<?> delete(@PathVariable Long id) {
        classService.delete(id);
        return Result.success("删除成功");
    }
}
