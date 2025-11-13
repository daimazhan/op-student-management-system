package com.example.studentmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.example.studentmanagement.annotation.LogOperation;
import com.example.studentmanagement.dto.StudentQueryDTO;
import com.example.studentmanagement.enums.OperationType;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;
import com.example.studentmanagement.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * 获取学生列表
     */
    @GetMapping("/list")
    @SaCheckPermission("student:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "学生管理", operationContent = "查询学生列表")
    public Result<Map<String, Object>> list(StudentQueryDTO queryDTO) {
        Map<String, Object> result = studentService.list(queryDTO);
        return Result.success(result);
    }

    /**
     * 获取学生详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("student:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "学生管理", operationContent = "查询学生详情")
    public Result<Student> getById(@PathVariable Long id) {
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    /**
     * 新增学生
     */
    @PostMapping
    @SaCheckPermission("student:add")
    @LogOperation(operationType = OperationType.ADD, operationModule = "学生管理", operationContent = "新增学生")
    public Result<?> save(@Valid @RequestBody Student student) {
        studentService.save(student);
        return Result.success("保存成功");
    }

    /**
     * 更新学生
     */
    @PutMapping
    @SaCheckPermission("student:edit")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "学生管理", operationContent = "更新学生")
    public Result<?> update(@Valid @RequestBody Student student) {
        studentService.update(student);
        return Result.success("更新成功");
    }

    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("student:delete")
    @LogOperation(operationType = OperationType.DELETE, operationModule = "学生管理", operationContent = "删除学生")
    public Result<?> delete(@PathVariable Long id) {
        studentService.delete(id);
        return Result.success("删除成功");
    }
}
