# SQL 注入靶场 - Spring Boot 自动建表 + 初始化数据

## ✅ 正确的实现思路

### 1️⃣ 配置文件说明

#### application.yml 关键配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sqli_training_lab?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

  sql:
    init:
      mode: always              # 每次启动都执行初始化
      platform: mysql           # 指定数据库平台
      schema-locations: classpath:schema.sql    # 建表脚本位置
      data-locations: classpath:data.sql        # 数据初始化脚本位置
      continue-on-error: false  # 出错时停止
```

### 2️⃣ SQL 脚本文件

#### schema.sql - 建表脚本
- ⚠️ **不要**包含 `CREATE DATABASE` 和 `USE` 语句
- Spring Boot 会自动连接到配置的数据库
- 只需创建表结构

```sql
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);
```

#### data.sql - 数据初始化脚本
- 插入初始测试数据
- 每条 INSERT 语句以分号结尾

```sql
INSERT INTO users (username, password) VALUES ('admin', 'admin123');
INSERT INTO users (username, password) VALUES ('test', 'test123');
INSERT INTO users (username, password) VALUES ('guest', 'guest123');
INSERT INTO users (username, password) VALUES ('admin\'--', 'hack');
```

### 3️⃣ 项目结构
```
src/main/resources/
├── application.yml      # 配置文件
├── schema.sql          # 建表脚本
└── data.sql           # 数据初始化脚本
```

### 4️⃣ 启动验证

应用启动后会自动：
1. 连接到 MySQL 数据库
2. 执行 `schema.sql` 创建表
3. 执行 `data.sql` 插入数据
4. 控制台输出验证信息

### 5️⃣ 访问接口

启动应用后，可以通过以下接口测试：

- **获取所有用户**: `GET http://localhost:8080/api/users`
- **安全查询**: `GET http://localhost:8080/api/users/admin`
- **不安全查询（SQL 注入演示）**: `GET http://localhost:8080/api/users/unsafe/admin'--`

### 6️⃣ 常见问题

#### ❌ 错误：表不存在
- 检查 `spring.sql.init.mode` 是否设置为 `always`
- 确认 `schema.sql` 中没有 `USE database` 语句

#### ❌ 错误：数据重复插入
- 使用 `INSERT IGNORE` 或 `ON DUPLICATE KEY UPDATE`
- 或者在插入前删除已有数据

#### ❌ 错误：数据库连接失败
- 确保 MySQL 服务已启动
- 检查数据库用户名密码是否正确
- 确认数据库 `sqli_training_lab` 已创建

### 7️⃣ 安全提示

⚠️ **本项目故意包含 SQL 注入漏洞用于教学目的**

- ❌ 不安全的代码：字符串拼接 SQL
- ✅ 安全的方式：使用参数化查询（PreparedStatement）

请参考 `UserRepository.java` 中的两种实现方式对比。
