package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentQueryDTO;
import com.example.studentmanagement.entity.Student;

import java.util.Map;

public interface StudentService {
    Map<String, Object> list(StudentQueryDTO queryDTO);
    Student getById(Long id);
    void save(Student student);
    void update(Student student);
    void delete(Long id);
}
