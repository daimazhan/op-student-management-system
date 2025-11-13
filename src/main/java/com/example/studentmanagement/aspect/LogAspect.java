package com.example.studentmanagement.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.example.studentmanagement.annotation.LogOperation;
import com.example.studentmanagement.entity.OperationLog;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.enums.OperationType;
import com.example.studentmanagement.service.OperationLogService;
import com.example.studentmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志切面
 * 用于自动记录操作日志
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final OperationLogService operationLogService;
    private final UserService userService;

    /**
     * 定义切点：所有标记了 @LogOperation 注解的方法
     */
    @Pointcut("@annotation(com.example.studentmanagement.annotation.LogOperation)")
    public void logPointcut() {
    }

    /**
     * 环绕通知：在方法执行前后记录日志
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            // 如果不是HTTP请求，直接执行方法
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        
        // 获取方法签名和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogOperation logOperation = method.getAnnotation(LogOperation.class);

        // 创建操作日志对象
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationTime(LocalDateTime.now());
        operationLog.setRequestMethod(request.getMethod());
        operationLog.setRequestUrl(request.getRequestURI());
        operationLog.setIpAddress(getIpAddress(request));

        // 获取当前用户信息（对于登录操作，在方法执行后再获取）
        // 先设置默认值，如果是登录操作，会在方法执行成功后更新
        Long userId = null;
        String username = null;
        boolean isLoginOperation = request.getRequestURI().contains("/login") && "POST".equals(request.getMethod());
        
        if (!isLoginOperation) {
            // 非登录操作，尝试获取当前登录用户信息
            try {
                if (StpUtil.isLogin()) {
                    userId = StpUtil.getLoginIdAsLong();
                    User user = userService.getById(userId);
                    if (user != null) {
                        username = user.getUsername();
                    }
                }
            } catch (Exception e) {
                // 如果获取用户信息失败，继续执行但不记录用户信息
            }
        }
        
        operationLog.setUserId(userId);
        operationLog.setUsername(username);

        // 设置操作类型
        OperationType operationTypeEnum = logOperation.operationType();
        String operationType;
        if (operationTypeEnum == OperationType.OTHER) {
            // 如果未指定，根据HTTP方法推断
            operationTypeEnum = OperationType.fromHttpMethod(request.getMethod());
        }
        operationType = operationTypeEnum.getCode();
        operationLog.setOperationType(operationType);

        // 设置操作模块
        String operationModule = logOperation.operationModule();
        if (operationModule == null || operationModule.isEmpty()) {
            operationModule = inferOperationModule(request.getRequestURI());
        }
        operationLog.setOperationModule(operationModule);

        // 设置操作内容
        String operationContent = logOperation.operationContent();
        if (operationContent == null || operationContent.isEmpty()) {
            operationContent = inferOperationContent(method.getName(), operationType, operationModule);
        }
        operationLog.setOperationContent(operationContent);

        // 特殊处理登录和登出操作
        if (operationTypeEnum == OperationType.LOGIN || operationTypeEnum == OperationType.LOGOUT) {
            operationLog.setOperationModule("认证授权");
            if (operationTypeEnum == OperationType.LOGIN) {
                operationLog.setOperationContent("用户登录");
            } else {
                operationLog.setOperationContent("用户登出");
            }
        }

        // 执行方法并记录结果
        Object result = null;
        try {
            result = joinPoint.proceed();
            operationLog.setStatus(1); // 成功
            
            // 如果是登录操作且成功，更新用户信息
            if (isLoginOperation && result != null) {
                try {
                    if (StpUtil.isLogin()) {
                        userId = StpUtil.getLoginIdAsLong();
                        User user = userService.getById(userId);
                        if (user != null) {
                            username = user.getUsername();
                            operationLog.setUserId(userId);
                            operationLog.setUsername(username);
                        }
                    }
                } catch (Exception e) {
                    // 获取用户信息失败不影响日志记录
                }
            }
        } catch (Exception e) {
            operationLog.setStatus(0); // 失败
            operationLog.setErrorMessage(e.getMessage());
            throw e; // 重新抛出异常
        } finally {
            // 异步保存日志（避免影响主流程）
            try {
                operationLogService.save(operationLog);
            } catch (Exception e) {
                // 日志保存失败不影响主流程
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 获取客户端IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }


    /**
     * 根据请求URL推断操作模块
     */
    private String inferOperationModule(String url) {
        if (url == null || url.isEmpty()) {
            return "未知模块";
        }

        // 移除查询参数
        String path = url.split("\\?")[0];
        
        // 根据路径推断模块
        if (path.contains("/student")) {
            return "学生管理";
        } else if (path.contains("/class")) {
            return "班级管理";
        } else if (path.contains("/user")) {
            return "用户管理";
        } else if (path.contains("/role")) {
            return "角色管理";
        } else if (path.contains("/menu")) {
            return "菜单管理";
        } else if (path.contains("/log")) {
            return "日志管理";
        } else if (path.contains("/auth")) {
            return "认证授权";
        } else {
            return "其他模块";
        }
    }

    /**
     * 根据方法名和操作类型推断操作内容
     */
    private String inferOperationContent(String methodName, String operationType, String operationModule) {
        String content = operationModule;
        
        OperationType type = OperationType.fromHttpMethod(operationType);
        if (type == OperationType.OTHER) {
            // 尝试通过操作类型代码查找
            for (OperationType opType : OperationType.values()) {
                if (opType.getCode().equals(operationType)) {
                    type = opType;
                    break;
                }
            }
        }
        
        switch (type) {
            case ADD:
                content += "-新增";
                break;
            case UPDATE:
                content += "-修改";
                break;
            case DELETE:
                content += "-删除";
                break;
            case QUERY:
                content += "-查询";
                break;
            case LOGIN:
                content = "用户登录";
                break;
            case LOGOUT:
                content = "用户登出";
                break;
            default:
                content += "-操作";
        }
        
        return content;
    }
}

