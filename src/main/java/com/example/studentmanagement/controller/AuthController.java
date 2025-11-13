package com.example.studentmanagement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.studentmanagement.annotation.LogOperation;
import com.example.studentmanagement.dto.ChangePasswordDTO;
import com.example.studentmanagement.enums.OperationType;
import com.example.studentmanagement.dto.LoginDTO;
import com.example.studentmanagement.dto.UserProfileDTO;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @LogOperation(operationType = OperationType.LOGIN, operationModule = "认证授权", operationContent = "用户登录")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if (user != null) {
            StpUtil.login(user.getId());
            String token = StpUtil.getTokenValue();
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", user.getUsername());
            data.put("role", user.getRole());
            
            return Result.success("登录成功", data);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/logout")
    @LogOperation(operationType = OperationType.LOGOUT, operationModule = "认证授权", operationContent = "用户登出")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.success("登出成功");
    }

    @GetMapping("/info")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "认证授权", operationContent = "查询用户信息")
    public Result<Map<String, Object>> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("realName", user.getRealName());
        data.put("email", user.getEmail());
        data.put("phone", user.getPhone());
        
        return Result.success(data);
    }

    /**
     * 更新当前用户的基本信息
     */
    @PutMapping("/profile")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "认证授权", operationContent = "更新个人信息")
    public Result<?> updateProfile(@Valid @RequestBody UserProfileDTO profileDTO) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            // 确保只能更新自己的信息
            profileDTO.setId(userId);
            User user = new User();
            user.setId(profileDTO.getId());
            user.setRealName(profileDTO.getRealName());
            user.setEmail(profileDTO.getEmail());
            user.setPhone(profileDTO.getPhone());
            userService.updateCurrentUser(user);
            return Result.success("更新成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改当前登录用户的密码
     */
    @PutMapping("/change-password")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "认证授权", operationContent = "修改密码")
    public Result<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            userService.changePassword(userId, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
            return Result.success("密码修改成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
