-- 清空旧数据（可选，确保每次启动数据一致）
DELETE FROM users;

-- 初始化用户数据
INSERT INTO users (username, password, email, status, description) VALUES ('admin', 'admin123', 'admin@example.com', 'active', 'Administrator account');
INSERT INTO users (username, password, email, status, description) VALUES ('test', 'test123', 'test@example.com', 'active', 'Test user account');
INSERT INTO users (username, password, email, status, description) VALUES ('guest', 'guest123', 'guest@example.com', 'inactive', 'Guest user account');
INSERT INTO users (username, password, email, status, description) VALUES ('admin\'--', 'hack', 'hack@example.com', 'active', 'Test account for SQL injection');