# 🚀 快速启动指南

## 前置要求

1. ✅ 已安装 JDK 17+
2. ✅ 已安装 MySQL 数据库
3. ✅ Maven 已配置

## 步骤 1：创建数据库

登录 MySQL 并创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS sqli_training_lab;
```

## 步骤 2：配置数据库连接

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sqli_training_lab?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root          # 改为你的 MySQL 用户名
    password: 123456        # 改为你的 MySQL 密码
```

## 步骤 3：启动应用

### Windows (PowerShell)
```powershell
.\mvnw.cmd spring-boot:run
```

### Linux/Mac
```bash
./mvnw spring-boot:run
```

或者使用 Maven：
```bash
mvn spring-boot:run
```

## 步骤 4：验证启动成功

启动后，控制台会显示：

```
========================================
✅ 数据库初始化验证开始...
✅ users 表已存在
✅ 成功加载 4 个用户:
   - ID: 1, Username: admin, Password: admin123
   - ID: 2, Username: test, Password: test123
   - ID: 3, Username: guest, Password: guest123
   - ID: 4, Username: admin'--, Password: hack
========================================
```

## 步骤 5：测试 API

### 获取所有用户
```bash
curl http://localhost:8080/api/users
```

### 查询特定用户（安全方式）
```bash
curl http://localhost:8080/api/users/admin
```

### SQL 注入演示（不安全）
```bash
curl "http://localhost:8080/api/users/unsafe/admin'--"
```

## 🔧 常见问题排查

### 问题 1：数据库连接失败
**错误信息**: `Access denied for user 'root'@'localhost'`

**解决方案**: 
- 检查 `application.yml` 中的用户名和密码是否正确
- 确保 MySQL 服务正在运行

### 问题 2：表不存在
**错误信息**: `Table 'sqli_training_lab.users' doesn't exist`

**解决方案**:
- 确认 `spring.sql.init.mode=always`
- 检查 `schema.sql` 文件是否存在
- 重启应用

### 问题 3：端口被占用
**错误信息**: `Port 8080 was already in use`

**解决方案**:
```bash
# Windows PowerShell
netstat -ano | findstr :8080
taskkill /PID <进程 ID> /F

# 或者修改端口
server.port=8081
```

## 📝 下一步

- 查看 `DATABASE_INIT_GUIDE.md` 了解详细的配置说明
- 查看 `UserRepository.java` 学习 SQL 注入的攻防对比
- 尝试使用 SQL 注入攻击接口进行测试
