package com.example.studentmanagement.mapper;

import com.example.studentmanagement.entity.ClassEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassMapper {
    List<ClassEntity> list();
    ClassEntity findById(@Param("id") Long id);
    void insert(ClassEntity classEntity);
    void update(ClassEntity classEntity);
    void deleteById(@Param("id") Long id);
}
