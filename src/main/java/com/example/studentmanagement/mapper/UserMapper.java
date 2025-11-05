package com.example.studentmanagement.mapper;

import com.example.studentmanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findById(@Param("id") Long id);
    List<User> list();
    void insert(User user);
    void update(User user);
    void deleteById(@Param("id") Long id);
    boolean existsByUsername(@Param("username") String username);
}
