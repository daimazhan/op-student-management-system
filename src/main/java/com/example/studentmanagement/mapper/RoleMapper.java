package com.example.studentmanagement.mapper;

import com.example.studentmanagement.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<Role> list();
    Role findById(@Param("id") Long id);
    Role findByRoleCode(@Param("roleCode") String roleCode);
    void insert(Role role);
    void update(Role role);
    void deleteById(@Param("id") Long id);
    boolean existsByRoleCode(@Param("roleCode") String roleCode, @Param("excludeId") Long excludeId);
}
