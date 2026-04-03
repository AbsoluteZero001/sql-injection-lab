-- 删除现有 users 表（如果存在）
DROP TABLE IF EXISTS users;

-- 创建 users 表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50),
    email VARCHAR(100),
    status VARCHAR(20),
    description TEXT
);