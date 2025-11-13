package com.example.studentmanagement.annotation;

import com.example.studentmanagement.enums.OperationType;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    /**
     * 操作类型
     * 如果不指定，则根据请求方法自动推断
     */
    OperationType operationType() default OperationType.OTHER;
    
    /**
     * 操作模块：学生管理、班级管理、用户管理等
     * 如果不指定，则根据请求URL自动推断
     */
    String operationModule() default "";
    
    /**
     * 操作内容描述
     * 如果不指定，则使用默认描述
     */
    String operationContent() default "";
}

