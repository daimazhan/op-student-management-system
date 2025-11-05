package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Menu;
import com.example.studentmanagement.mapper.MenuMapper;
import com.example.studentmanagement.mapper.RoleMenuMapper;
import com.example.studentmanagement.service.MenuService;
import com.example.studentmanagement.vo.MenuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public List<MenuVO> getMenuTree() {
        List<Menu> menus = menuMapper.list();
        // 导航菜单需要过滤按钮类型
        return buildMenuTree(menus, 0L, true);
    }

    @Override
    public List<MenuVO> getMenuTreeByRole(Long roleId) {
        List<Menu> menus = menuMapper.listByRole(roleId);
        return buildMenuTree(menus, 0L, true);
    }

    @Override
    public List<MenuVO> getAllMenuTree() {
        List<Menu> menus = menuMapper.list();
        // 权限分配时需要包含所有菜单（包括按钮类型）
        return buildMenuTree(menus, 0L, false);
    }

    /**
     * 构建菜单树
     * @param menus 菜单列表
     * @param parentId 父菜单ID
     * @param filterButton 是否过滤按钮类型（true-过滤，false-不过滤）
     * @return 菜单树
     */
    private List<MenuVO> buildMenuTree(List<Menu> menus, Long parentId, boolean filterButton) {
        List<MenuVO> tree = new ArrayList<>();
        for (Menu menu : menus) {
            // 如果filterButton为true，过滤掉按钮类型（type=2）的菜单，按钮不应该显示在导航菜单中
            if (filterButton && menu.getType() != null && menu.getType() == 2) {
                continue;
            }
            if (parentId.equals(menu.getParentId() == null ? 0L : menu.getParentId())) {
                MenuVO menuVO = convertToVO(menu);
                List<MenuVO> children = buildMenuTree(menus, menu.getId(), filterButton);
                if (!children.isEmpty()) {
                    menuVO.setChildren(children);
                }
                tree.add(menuVO);
            }
        }
        return tree;
    }

    private MenuVO convertToVO(Menu menu) {
        MenuVO vo = new MenuVO();
        vo.setId(menu.getId());
        vo.setName(menu.getName());
        vo.setPath(menu.getPath());
        vo.setComponent(menu.getComponent());
        vo.setIcon(menu.getIcon());
        vo.setParentId(menu.getParentId());
        vo.setSort(menu.getSort());
        vo.setType(menu.getType());
        vo.setPermission(menu.getPermission());
        return vo;
    }

    @Override
    public List<Menu> list() {
        return menuMapper.list();
    }

    @Override
    public Menu getById(Long id) {
        return menuMapper.findById(id);
    }

    @Override
    public void save(Menu menu) {
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        menuMapper.insert(menu);
    }

    @Override
    public void update(Menu menu) {
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.update(menu);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 删除角色菜单关联
        roleMenuMapper.deleteByMenuId(id);
        // 删除菜单
        menuMapper.deleteById(id);
    }


}
