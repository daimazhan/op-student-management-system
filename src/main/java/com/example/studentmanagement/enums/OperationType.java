package com.example.studentmanagement.enums;

/**
 * 操作类型枚举
 */
public enum OperationType {
    /**
     * 新增
     */
    ADD("ADD", "新增"),
    
    /**
     * 修改
     */
    UPDATE("UPDATE", "修改"),
    
    /**
     * 删除
     */
    DELETE("DELETE", "删除"),
    
    /**
     * 查询
     */
    QUERY("QUERY", "查询"),
    
    /**
     * 登录
     */
    LOGIN("LOGIN", "登录"),
    
    /**
     * 登出
     */
    LOGOUT("LOGOUT", "登出"),
    
    /**
     * 其他操作
     */
    OTHER("OTHER", "其他");

    private final String code;
    private final String description;

    OperationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据HTTP方法推断操作类型
     */
    public static OperationType fromHttpMethod(String httpMethod) {
        if (httpMethod == null) {
            return OTHER;
        }
        return switch (httpMethod.toUpperCase()) {
            case "GET" -> QUERY;
            case "POST" -> ADD;
            case "PUT" -> UPDATE;
            case "DELETE" -> DELETE;
            default -> OTHER;
        };
    }
}

