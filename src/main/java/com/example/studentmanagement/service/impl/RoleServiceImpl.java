package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.entity.RoleMenu;
import com.example.studentmanagement.mapper.RoleMapper;
import com.example.studentmanagement.mapper.RoleMenuMapper;
import com.example.studentmanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public List<Role> list() {
        return roleMapper.list();
    }

    @Override
    public Role getById(Long id) {
        return roleMapper.findById(id);
    }

    @Override
    public Role getByRoleCode(String roleCode) {
        return roleMapper.findByRoleCode(roleCode);
    }

    @Override
    @Transactional
    public void save(Role role) {
        // 检查角色编码是否已存在
        if (roleMapper.existsByRoleCode(role.getRoleCode(), null)) {
            throw new RuntimeException("角色编码已存在");
        }
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        if (role.getStatus() == null) {
            role.setStatus(1);
        }
        roleMapper.insert(role);
    }

    @Override
    @Transactional
    public void update(Role role) {
        Role existingRole = roleMapper.findById(role.getId());
        if (existingRole == null) {
            throw new RuntimeException("角色不存在");
        }
        // 如果角色编码改变了，检查新角色编码是否已存在
        if (!existingRole.getRoleCode().equals(role.getRoleCode()) &&
            roleMapper.existsByRoleCode(role.getRoleCode(), role.getId())) {
            throw new RuntimeException("角色编码已存在");
        }
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.update(role);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查是否有用户使用该角色
        // TODO: 可以添加检查逻辑
        // 删除角色菜单关联
        roleMenuMapper.deleteByRole(id);
        // 删除角色
        roleMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void assignMenuToRole(Long roleId, List<Long> menuIds) {
        // 先删除该角色的所有菜单关联
        roleMenuMapper.deleteByRole(roleId);
        // 重新分配菜单
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }
        }
    }

    @Override
    public List<Long> getMenuIdsByRole(Long roleId) {
        return roleMenuMapper.findMenuIdsByRole(roleId);
    }
}
