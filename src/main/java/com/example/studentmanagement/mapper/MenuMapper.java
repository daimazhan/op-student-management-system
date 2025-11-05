package com.example.studentmanagement.mapper;

import com.example.studentmanagement.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> list();
    List<Menu> listByRole(@Param("role") Long roleId);  // 改为使用 roleId
    Menu findById(@Param("id") Long id);
    void insert(Menu menu);
    void update(Menu menu);
    void deleteById(@Param("id") Long id);
    List<Menu> listByParentId(@Param("parentId") Long parentId);
}
