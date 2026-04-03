# SQL Injection Lab

一个基于 Spring Boot 构建的 SQL 注入学习与测试靶场项目，用于帮助理解和实践常见的 SQL 注入攻击方式。

> ⚠️ 本项目仅用于学习、测试与安全研究，请勿用于非法用途。

---

## 📌 项目简介

本项目提供一个可控的 Web 环境，用于模拟常见的 SQL 注入场景，包括：

- 回显注入（Error-based / Union-based）
- 布尔盲注（Boolean-based Blind）
- 时间盲注（Time-based Blind）
- 报错注入（Error-based Injection）

通过该项目可以：

- 学习 SQL 注入原理
- 理解漏洞产生原因
- 掌握基础利用方式
- 配合工具（如 Postman / Burp Suite）进行测试

---

## 🧱 技术栈

### 📌 核心框架

- Spring Boot: 3.5.13
- Java: 17

### 📌 主要依赖

#### 后端框架

- Spring Web: 3.5.13 (构建 RESTful API 和 Web 应用)
- Spring Security: 3.5.13 (安全认证与授权)
- Spring Data JPA: 3.5.13 (数据持久层框架)

#### 前端模板

- Thymeleaf: 3.5.13 (服务器端模板引擎)

#### 数据库相关

- MySQL Connector/J: 最新兼容版本 (MySQL 数据库驱动)
- 数据库: MySQL (通过 JDBC 连接)

#### 开发工具

- Lombok: 最新版本 (简化代码编写)
- Spring Boot Configuration Processor: 3.5.13 (配置元数据支持)

#### 文档工具

- SpringDoc OpenAPI UI: 2.8.16 (Swagger/OpenAPI 文档生成)

#### 测试框架

- Spring Boot Starter Test: 3.5.13 (集成测试)
- Spring Security Test: 3.5.13 (安全测试)
- JUnit: (通过 Spring Boot Test 引入)

### 📌 构建工具

- Maven:
  - Maven Compiler Plugin (编译插件)
  - Spring Boot Maven Plugin (打包插件)

### 📌 数据库配置

- 连接 URL: jdbc:mysql://localhost:3306/sqli_training_lab
- 初始化模式：启动时自动执行 schema.sql 和 data.sql

### 📌 项目特点

这是一个基于 Spring Boot 3.5 + Java 17 的单体架构 SQL 注入漏洞演练平台，使用 MySQL 作为训练数据库，通过 Thymeleaf 提供前端页面，适合进行 SQL 注入攻防学习和测试。

---

## 📂 项目结构（示例）
