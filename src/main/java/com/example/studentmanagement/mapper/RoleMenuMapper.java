package com.example.studentmanagement.mapper;

import com.example.studentmanagement.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper {
    void insert(RoleMenu roleMenu);
    void deleteByRole(@Param("role") Long roleId);  // 改为使用 roleId
    void deleteByMenuId(@Param("menuId") Long menuId);
    List<Long> findMenuIdsByRole(@Param("role") Long roleId);  // 改为使用 roleId
}
