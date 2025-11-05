package com.example.studentmanagement.mapper;

import com.example.studentmanagement.dto.StudentQueryDTO;
import com.example.studentmanagement.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> list(@Param("query") StudentQueryDTO queryDTO, 
                      @Param("offset") Integer offset, 
                      @Param("limit") Integer limit);
    Long count(@Param("query") StudentQueryDTO queryDTO);
    Student findById(@Param("id") Long id);
    void insert(Student student);
    void update(Student student);
    void deleteById(@Param("id") Long id);
}
