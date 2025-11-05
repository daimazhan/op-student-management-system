package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {
    /**
     * 获取角色列表
     * @return 角色列表
     */
    List<Role> list();

    /**
     * 根据ID获取角色
     * @param id 角色ID
     * @return 角色信息
     */
    Role getById(Long id);

    /**
     * 根据角色编码获取角色
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role getByRoleCode(String roleCode);

    /**
     * 新增角色
     * @param role 角色信息
     */
    void save(Role role);
    /**
     * 更新角色
     * @param role 角色信息
     */
    void update(Role role);

    /**
     * 删除角色
     * @param id 角色ID
     */
    void delete(Long id);

    /**
     * 分配菜单给角色
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    void assignMenuToRole(Long roleId, List<Long> menuIds);

    /**
     * 获取角色已分配的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getMenuIdsByRole(Long roleId);
}