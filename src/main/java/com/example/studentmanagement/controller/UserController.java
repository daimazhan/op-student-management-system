package com.example.studentmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.example.studentmanagement.annotation.LogOperation;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.enums.OperationType;
import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    @SaCheckPermission("user:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "用户管理", operationContent = "查询用户列表")
    public Result<List<User>> list() {
        List<User> list = userService.list();
        return Result.success(list);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("user:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "用户管理", operationContent = "查询用户详情")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping
    @SaCheckPermission("user:add")
    @LogOperation(operationType = OperationType.ADD, operationModule = "用户管理", operationContent = "新增用户")
    public Result<?> save(@Valid @RequestBody User user) {
        try {
            userService.save(user);
            return Result.success("保存成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping
    @SaCheckPermission("user:edit")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "用户管理", operationContent = "更新用户")
    public Result<?> update(@Valid @RequestBody User user) {
        try {
            userService.update(user);
            return Result.success("更新成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("user:delete")
    @LogOperation(operationType = OperationType.DELETE, operationModule = "用户管理", operationContent = "删除用户")
    public Result<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    @SaCheckPermission("user:reset")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "用户管理", operationContent = "重置用户密码")
    public Result<?> resetPassword(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String newPassword = (String) params.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            newPassword = "123456"; // 默认密码
        }
        try {
            userService.resetPassword(id, newPassword);
            return Result.success("密码重置成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
