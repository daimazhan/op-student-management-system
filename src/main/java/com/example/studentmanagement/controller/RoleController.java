package com.example.studentmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.example.studentmanagement.annotation.LogOperation;
import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.enums.OperationType;
import com.example.studentmanagement.service.RoleService;
import com.example.studentmanagement.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 获取角色列表
     */
    @GetMapping("/list")
    @SaCheckPermission("role:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "角色管理", operationContent = "查询角色列表")
    public Result<List<Role>> list() {
        List<Role> list = roleService.list();
        return Result.success(list);
    }

    /**
     * 获取角色详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("role:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "角色管理", operationContent = "查询角色详情")
    public Result<Role> getById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return Result.success(role);
    }

    /**
     * 新增角色
     */
    @PostMapping
    @SaCheckPermission("role:add")
    @LogOperation(operationType = OperationType.ADD, operationModule = "角色管理", operationContent = "新增角色")
    public Result<?> save(@Valid @RequestBody Role role) {
        try {
            roleService.save(role);
            return Result.success("保存成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @PutMapping
    @SaCheckPermission("role:edit")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "角色管理", operationContent = "更新角色")
    public Result<?> update(@Valid @RequestBody Role role) {
        try {
            roleService.update(role);
            return Result.success("更新成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("role:delete")
    @LogOperation(operationType = OperationType.DELETE, operationModule = "角色管理", operationContent = "删除角色")
    public Result<?> delete(@PathVariable Long id) {
        try {
            roleService.delete(id);
            return Result.success("删除成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分配菜单给角色
     */
    @PostMapping("/assign-menu")
    @SaCheckPermission("role:assign")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "角色管理", operationContent = "分配菜单权限")
    public Result<?> assignMenuToRole(@RequestBody Map<String, Object> params) {
        Long roleId = Long.valueOf(params.get("roleId").toString());
        @SuppressWarnings("unchecked")
        List<Object> menuIdsObj = (List<Object>) params.get("menuIds");
        
        // 将 List<Object> 转换为 List<Long>
        List<Long> menuIds = new ArrayList<>();
        if (menuIdsObj != null) {
            for (Object obj : menuIdsObj) {
                if (obj instanceof Number) {
                    menuIds.add(((Number) obj).longValue());
                } else if (obj instanceof String) {
                    menuIds.add(Long.parseLong((String) obj));
                }
            }
        }
        
        roleService.assignMenuToRole(roleId, menuIds);
        return Result.success("分配成功");
    }

    /**
     * 获取角色已分配的菜单ID列表
     */
    @GetMapping("/{roleId}/menus")
    @SaCheckPermission("role:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "角色管理", operationContent = "查询角色菜单权限")
    public Result<List<Long>> getMenuIdsByRole(@PathVariable Long roleId) {
        List<Long> menuIds = roleService.getMenuIdsByRole(roleId);
        return Result.success(menuIds);
    }
}
