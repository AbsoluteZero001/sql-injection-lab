-- 删除现有表（如果存在）
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS users;

-- 创建 users 表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50),
    email VARCHAR(100),
    status VARCHAR(20),
    role VARCHAR(20) DEFAULT 'user', -- user, admin
    description TEXT
);

-- 创建 comments 表
CREATE TABLE IF NOT EXISTS comments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    content TEXT,
    status VARCHAR(20) DEFAULT 'pending', -- pending, approved, rejected
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);