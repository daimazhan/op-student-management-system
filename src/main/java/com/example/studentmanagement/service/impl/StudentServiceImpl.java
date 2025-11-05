package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.StudentQueryDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.mapper.StudentMapper;
import com.example.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;

    @Override
    public Map<String, Object> list(StudentQueryDTO queryDTO) {
        int offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();
        List<Student> list = studentMapper.list(queryDTO, offset, queryDTO.getPageSize());
        Long total = studentMapper.count(queryDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNum", queryDTO.getPageNum());
        result.put("pageSize", queryDTO.getPageSize());
        return result;
    }

    @Override
    public Student getById(Long id) {
        return studentMapper.findById(id);
    }

    @Override
    public void save(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void delete(Long id) {
        studentMapper.deleteById(id);
    }
}
