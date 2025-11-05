package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.ClassEntity;
import com.example.studentmanagement.mapper.ClassMapper;
import com.example.studentmanagement.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassMapper classMapper;

    @Override
    public List<ClassEntity> list() {
        return classMapper.list();
    }

    @Override
    public ClassEntity getById(Long id) {
        return classMapper.findById(id);
    }

    @Override
    public void save(ClassEntity classEntity) {
        classEntity.setCreateTime(LocalDateTime.now());
        classEntity.setUpdateTime(LocalDateTime.now());
        classMapper.insert(classEntity);
    }

    @Override
    public void update(ClassEntity classEntity) {
        classEntity.setUpdateTime(LocalDateTime.now());
        classMapper.update(classEntity);
    }

    @Override
    public void delete(Long id) {
        classMapper.deleteById(id);
    }
}
