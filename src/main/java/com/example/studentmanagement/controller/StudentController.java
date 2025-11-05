package com.example.studentmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.example.studentmanagement.dto.StudentQueryDTO;
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
    public Result<Map<String, Object>> list(StudentQueryDTO queryDTO) {
        Map<String, Object> result = studentService.list(queryDTO);
        return Result.success(result);
    }

    /**
     * 获取学生详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("student:view")
    public Result<Student> getById(@PathVariable Long id) {
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    /**
     * 新增学生
     */
    @PostMapping
    @SaCheckPermission("student:add")
    public Result<?> save(@Valid @RequestBody Student student) {
        studentService.save(student);
        return Result.success("保存成功");
    }

    /**
     * 更新学生
     */
    @PutMapping
    @SaCheckPermission("student:edit")
    public Result<?> update(@Valid @RequestBody Student student) {
        studentService.update(student);
        return Result.success("更新成功");
    }

    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("student:delete")
    public Result<?> delete(@PathVariable Long id) {
        studentService.delete(id);
        return Result.success("删除成功");
    }
}
