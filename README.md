# 学生管理系统

基于 Spring Boot 3.x + Vue 3 + Element Plus 的全栈学生管理系统

![dashboard](/doc/2.png)
![login](/doc/1.png)
![log](/doc/3.png)
![student](/doc/4.png)

## 技术栈

### 后端
- Spring Boot 3.2.0
- MyBatis
- MySQL 8.0
- Sa-Token (登录鉴权)
- Lombok
- Spring Validation

### 前端
- Vue 3
- Element Plus
- Vite
- Vue Router
- Pinia
- Axios

## 项目结构

```
当前根目录
├── src/                    # 后端代码
│   ├── main/
│   │   ├── java/com/example/studentmanagement/
│   │   │   ├── controller/    # 控制器
│   │   │   ├── service/       # 服务层
│   │   │   ├── mapper/        # Mapper接口
│   │   │   ├── entity/        # 实体类
│   │   │   ├── dto/           # 数据传输对象
│   │   │   ├── vo/            # 视图对象
│   │   │   └── config/        # 配置类
│   │   └── resources/
│   │       ├── mapper/        # MyBatis XML
│   │       ├── db/            # 数据库脚本
│   │       └── application.yml
│   └── test/
└── web/                    # 前端代码
    ├── src/
    │   ├── api/             # API接口
    │   ├── assets/          # 静态资源
    │   ├── components/      # 公共组件
    │   ├── router/          # 路由配置
    │   ├── store/           # 状态管理
    │   ├── utils/           # 工具类
    │   └── views/           # 页面组件
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 快速开始

### 1. 数据库配置

执行 `src/main/resources/db/schema.sql` 创建数据库和表结构。

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
```

### 2. 后端启动

```bash
cd student-management-system
mvn clean install
mvn spring-boot:run
```

后端服务运行在 `http://localhost:8080`

### 3. 前端启动

```bash
cd web
npm install
npm run dev
```

前端服务运行在 `http://localhost:5173`

## 默认账号

- 管理员：admin / admin123
- 普通用户：user / user123

## 功能特性

- ✅ 用户登录/登出
- ✅ 学生信息管理（增删改查、分页、模糊搜索）
- ✅ 班级管理（增删改查）
- ✅ 权限控制（基于 Sa-Token）
- ✅ 统一异常处理
- ✅ 参数校验
- ✅ 前后端分离架构
- ✅ RESTful API

## API 接口

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/info` - 获取用户信息

### 学生管理
- `GET /api/student/list` - 获取学生列表（支持分页和搜索）
- `GET /api/student/{id}` - 获取学生详情
- `POST /api/student` - 新增学生
- `PUT /api/student` - 更新学生
- `DELETE /api/student/{id}` - 删除学生

### 班级管理
- `GET /api/class/list` - 获取班级列表
- `GET /api/class/{id}` - 获取班级详情
- `POST /api/class` - 新增班级
- `PUT /api/class` - 更新班级
- `DELETE /api/class/{id}` - 删除班级

## 开发说明

### 后端开发
- Controller 层处理 HTTP 请求
- Service 层处理业务逻辑
- Mapper 层处理数据库操作
- 使用 `Result<T>` 统一响应结构
- 使用 `@Valid` 进行参数校验

### 前端开发
- API 请求统一封装在 `src/api/` 目录
- 使用 Pinia 进行状态管理
- 使用 Vue Router 进行路由管理
- 使用 Element Plus 组件库

## 技术指导
微信：daimazhan
