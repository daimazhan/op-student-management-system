package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.ClassEntity;

import java.util.List;

public interface ClassService {
    List<ClassEntity> list();
    ClassEntity getById(Long id);
    void save(ClassEntity classEntity);
    void update(ClassEntity classEntity);
    void delete(Long id);
}
