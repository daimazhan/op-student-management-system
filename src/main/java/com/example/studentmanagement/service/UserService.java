package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);
    User getById(Long id);
    String getRoleByUserId(Long userId);  // 返回 role_code
    Long getRoleIdByUserId(Long userId);  // 新增：返回 role_id
    List<User> list();
    void save(User user);
    void update(User user);
    void delete(Long id);
    boolean existsByUsername(String username);
    void resetPassword(Long id, String newPassword);
    /**
     * 更新当前登录用户的基本信息（不包含密码和角色）
     * @param user 用户信息
     */
    void updateCurrentUser(User user);
    /**
     * 修改当前登录用户的密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
}
