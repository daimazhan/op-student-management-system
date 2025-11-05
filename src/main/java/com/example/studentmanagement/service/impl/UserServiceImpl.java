package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.mapper.RoleMapper;
import com.example.studentmanagement.mapper.UserMapper;
import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.utils.Md5Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && Md5Util.verify(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User getById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public String getRoleByUserId(Long userId) {
        User user = userMapper.findById(userId);
        return user != null ? user.getRole() : "USER";
    }

    @Override
    public Long getRoleIdByUserId(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null || user.getRole() == null) {
            // 返回默认 USER 角色的 ID
            var defaultRole = roleMapper.findByRoleCode("USER");
            return defaultRole != null ? defaultRole.getId() : null;
        }
        var role = roleMapper.findByRoleCode(user.getRole());
        return role != null ? role.getId() : null;
    }

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    @Transactional
    public void save(User user) {
        // 检查用户名是否已存在
        if (userMapper.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // 如果密码为空，设置默认密码
        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            password = "123456"; // 默认密码
        }
        // 使用MD5加密密码
        user.setPassword(Md5Util.md5(password));
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        User existingUser = userMapper.findById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        // 如果用户名改变了，检查新用户名是否已存在
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            userMapper.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        user.setUpdateTime(LocalDateTime.now());
        // 如果密码为空，不更新密码；如果提供了新密码，使用MD5加密
        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            // 新密码使用MD5加密
            user.setPassword(Md5Util.md5(password));
        }
        userMapper.update(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMapper.existsByUsername(username);
    }

    @Override
    @Transactional
    public void resetPassword(Long id, String newPassword) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 如果新密码为空，设置默认密码
        if (newPassword == null || newPassword.isEmpty()) {
            newPassword = "123456";
        }
        // 使用MD5加密密码
        user.setPassword(Md5Util.md5(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    @Transactional
    public void updateCurrentUser(User user) {
        User existingUser = userMapper.findById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        // 只更新基本信息，不允许修改用户名、密码和角色
        existingUser.setRealName(user.getRealName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setUpdateTime(LocalDateTime.now());
        userMapper.update(existingUser);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 验证旧密码
        if (!Md5Util.verify(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }
        // 使用MD5加密新密码
        user.setPassword(Md5Util.md5(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }
}
