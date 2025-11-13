package com.example.studentmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.example.studentmanagement.annotation.LogOperation;
import com.example.studentmanagement.entity.Menu;
import com.example.studentmanagement.enums.OperationType;
import com.example.studentmanagement.service.MenuService;
import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.vo.MenuVO;
import com.example.studentmanagement.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final UserService userService;

    /**
     * 获取当前用户的菜单树
     */
    @GetMapping("/tree")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "菜单管理", operationContent = "查询当前用户菜单")
    public Result<List<MenuVO>> getMenuTree() {
        // 获取当前用户角色ID
        Long userId = StpUtil.getLoginIdAsLong();
        Long roleId = userService.getRoleIdByUserId(userId);
        List<MenuVO> menuTree = menuService.getMenuTreeByRole(roleId);
        return Result.success(menuTree);
    }

    /**
     * 获取所有菜单树（管理员，包含按钮类型，用于权限分配）
     */
    @GetMapping("/tree/all")
    @SaCheckPermission("menu:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "菜单管理", operationContent = "查询所有菜单树")
    public Result<List<MenuVO>> getAllMenuTree() {
        List<MenuVO> menuTree = menuService.getAllMenuTree();
        return Result.success(menuTree);
    }

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    @SaCheckPermission("menu:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "菜单管理", operationContent = "查询菜单列表")
    public Result<List<Menu>> list() {
        List<Menu> list = menuService.list();
        return Result.success(list);
    }

    /**
     * 获取菜单详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("menu:view")
    @LogOperation(operationType = OperationType.QUERY, operationModule = "菜单管理", operationContent = "查询菜单详情")
    public Result<Menu> getById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    /**
     * 新增菜单
     */
    @PostMapping
    @SaCheckPermission("menu:add")
    @LogOperation(operationType = OperationType.ADD, operationModule = "菜单管理", operationContent = "新增菜单")
    public Result<?> save(@Valid @RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success("保存成功");
    }

    /**
     * 更新菜单
     */
    @PutMapping
    @SaCheckPermission("menu:edit")
    @LogOperation(operationType = OperationType.UPDATE, operationModule = "菜单管理", operationContent = "更新菜单")
    public Result<?> update(@Valid @RequestBody Menu menu) {
        menuService.update(menu);
        return Result.success("更新成功");
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("menu:delete")
    @LogOperation(operationType = OperationType.DELETE, operationModule = "菜单管理", operationContent = "删除菜单")
    public Result<?> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success("删除成功");
    }




}
