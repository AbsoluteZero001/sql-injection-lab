# SQL Injection Lab

一个基于 Spring Boot 3 + MySQL + Thymeleaf 的 Web 漏洞学习靶场，包含一个模拟真实业务的内容管理系统，以及独立的 SQL 注入、XSS、目录探测练习区。

> 本项目仅用于学习、授权测试与安全研究，禁止对非授权目标使用。

## 功能概览

### 业务功能中的注入点

- `/login`：用户名、密码校验，用户名参数直接拼接进 `SELECT`，可用于登录绕过、报错注入与 Union 注入。
- `/register`：用户名/邮箱查重与插入语句均使用字符串拼接。
- `/user/addComment`：评论内容直接拼接进 `INSERT`，同时评论会被后台原样渲染，可观察存储型 XSS。
- `/user/updateProfile`：个人资料更新条件与字段均拼接用户输入。
- `/user/home`、`/user/comments`、`/admin/comments`：会话 ID/评论 ID 直接拼接。

### 独立实验区（首页登录框下方进入）

| 路径 | 类型 |
| --- | --- |
| `/lab` | 实验区总览 |
| `/lab/union` | Union/回显注入，通过存储过程 `lab_search_users` 搜索用户 |
| `/lab/sql/error` | MySQL updatexml/extractvalue 报错注入，参数进入存储过程 `lab_user_by_id` |
| `/lab/sql/boolean` | 布尔盲注，SQL 在存储过程内部拼接 |
| `/lab/sql/time` | 时间盲注，SQL 在存储过程内部拼接 |
| `/lab/xss/reflected` | 反射型 XSS |
| `/lab/xss/stored` | 存储型 XSS 留言板 |
| `/lab/discovery` | 目录爆破与隐藏资源挑战 |

### 隐藏静态靶点

站点保留了一批未出现在导航中的静态页面与文件，用于练习目录枚举、robots 信息泄露、备份文件与源码信息泄露。起点是公开的 `/robots.txt`，目标文件位于 `src/main/resources/static/` 下的常见目录名中。

推荐练习工具：`dirsearch`、`gobuster`、`ffuf`、Burp Intruder。命令行示例：

```bash
dirsearch -u http://localhost:8080 -w directory-list-2.3-medium.txt
gobuster dir -u http://localhost:8080 -w common.txt
```

## 快速启动

1. 创建 MySQL 数据库：

```sql
CREATE DATABASE sqli_training_lab DEFAULT CHARACTER SET utf8mb4;
```

2. 按需修改 `SQLInjectionLab/src/main/resources/application.yml` 中的数据库账号密码。
3. 启动应用：

```bash
cd SQLInjectionLab
./mvnw spring-boot:run
```

应用会执行 `schema.sql` 与 `data.sql` 重建训练数据，默认地址为 `http://localhost:8080/`。

## 建议练习顺序

1. 登录页使用 `admin' -- ` 或用户名恒真条件观察认证绕过。
2. 在 `/lab/union` 使用 `%' UNION SELECT ... -- ` 练习列数探测和数据回显。
3. 使用 `updatexml`、`extractvalue` 练习报错注入。
4. 使用真假条件与 `SLEEP` 完成布尔盲注、时间盲注。
5. 在留言板提交 `<script>` 或事件载荷，观察存储型 XSS。
6. 结合 `/robots.txt` 与目录字典发现隐藏静态页面。

## 技术说明

- Java 17 + Spring Boot 3
- Spring MVC + Thymeleaf
- Spring Security（仅关闭默认表单认证，页面鉴权由会话工具类模拟）
- MySQL 8 + Spring JDBC `JdbcTemplate`
- 业务区（登录/注册/评论/资料）保留 Java 侧 SQL 字符串拼接
- `/lab/*` 的注入接口通过 `CALL` 调用 MySQL 存储过程，动态 SQL 在数据库内部用 `CONCAT`/`PREPARE` 拼接，模拟“SQL 不在应用代码里”的真实业务形态

存储过程定义见 `LabProcedureBootstrap.java`，每次启动重建，包含 `lab_search_users` 与 `lab_user_by_id` 两个教学用动态 SQL 过程。

> 部署到外部环境前，请务必用参数化查询、白名单过滤和 HTML 转义替换示例中的危险写法。
