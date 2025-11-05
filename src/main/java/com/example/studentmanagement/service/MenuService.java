package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Menu;
import com.example.studentmanagement.vo.MenuVO;

import java.util.List;

public interface MenuService {
    List<MenuVO> getMenuTree();
    List<MenuVO> getMenuTreeByRole(Long roleId);  // 改为使用 roleId
    List<MenuVO> getAllMenuTree(); // 获取包含所有菜单（包括按钮）的菜单树，用于权限分配
    List<Menu> list();
    Menu getById(Long id);
    void save(Menu menu);
    void update(Menu menu);
    void delete(Long id);
}
