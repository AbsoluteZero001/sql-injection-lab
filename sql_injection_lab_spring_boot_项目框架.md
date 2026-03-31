# SQL Injection Lab - Spring Boot 项目框架

以下为一个可在 IntelliJ IDEA 中直接创建的 Spring Boot 项目基础框架（适用于 Java 17 + Spring Boot 3.x + MyBatis + MySQL）。

---

# 📁 一、项目结构

```
sql-injection-lab
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.sqli
│   │   │       ├── SqlInjectionLabApplication.java
│   │   │       ├── controller
│   │   │       │   └── UserController.java
│   │   │       ├── service
│   │   │       │   ├── UserService.java
│   │   │       │   └── impl
│   │   │       │       └── UserServiceImpl.java
│   │   │       ├── mapper
│   │   │       │   └── UserMapper.java
│   │   │       ├── entity
│   │   │       │   └── User.java
│   │   │       └── dto
│   │   │           └── UserDTO.java
│   │   ├── resources
│   │   │   ├── mapper
│   │   │   │   └── UserMapper.xml
│   │   │   ├── application.yml
│   │   │   └── sql
│   │   │       └── init.sql
│   └── test
├── pom.xml
└── README.md
```

---

# ⚙️ 二、pom.xml（核心依赖）

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>sql-injection-lab</artifactId>
    <version>1.0.0</version>

    <properties>
        <java.version>17</java.version>
        <spring.boot.version>3.2.4</spring.boot.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- MyBatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>3.0.3</version>
        </dependency>

        <!-- MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

# ⚙️ 三、application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sql_lab?useSSL=false&serverTimezone=UTC
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.sqli.entity
```

---

# 🚀 四、启动类

```java
package com.example.sqli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SqlInjectionLabApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqlInjectionLabApplication.class, args);
    }
}
```

---

# 🧩 五、Entity

```java
package com.example.sqli.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
}
```

---

# 🧩 六、Mapper

```java
package com.example.sqli.mapper;

import com.example.sqli.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    // ❗ 漏洞点：字符串拼接（用于SQL注入测试）
    @Select("SELECT * FROM users WHERE id = ${id}")
    User findById(Integer id);
}
```

---

# 🧩 七、Service

```java
package com.example.sqli.service;

import com.example.sqli.entity.User;

public interface UserService {
    User getUserById(Integer id);
}
```

```java
package com.example.sqli.service.impl;

import com.example.sqli.entity.User;
import com.example.sqli.mapper.UserMapper;
import com.example.sqli.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserById(Integer id) {
        return userMapper.findById(id);
    }
}
```

---

# 🧩 八、Controller（漏洞入口）

```java
package com.example.sqli.controller;

import com.example.sqli.entity.User;
import com.example.sqli.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/get")
    public User getUser(@RequestParam Integer id) {
        return userService.getUserById(id);
    }
}
```

---

# 🧪 九、数据库初始化 SQL

```sql
CREATE DATABASE sql_lab;
USE sql_lab;

CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50),
  password VARCHAR(50)
);

INSERT INTO users(username, password) VALUES
('admin', 'admin123'),
('test', 'test123');
```

---

# 🔥 十、测试接口

启动后访问：

```
http://localhost:8080/user/get?id=1
```

可尝试注入：

```
?id=1 OR 1=1
?id=1 UNION SELECT 1,2,3
```

---

# ✅ 下一步建议

你可以在这个框架上扩展：

- 新增多个漏洞接口（布尔盲注 / 时间盲注）
- 加入登录接口（模拟认证注入）
- 增加错误回显场景
- 分模块（Level1 / Level2 / Level3）靶场
- 接入 Postman / Burp 测试脚本

---

如果需要，我可以帮你把这个项目升级成「完整靶场分级系统（类似 SQLi-Labs）」或者帮你设计“训练关卡 + 难度递进 + 自动提示系统”。
