-- 清空旧数据（可选，确保每次启动数据一致）
DELETE FROM users;

-- 初始化用户数据
INSERT INTO users (username, password) VALUES ('admin', 'admin123');
INSERT INTO users (username, password) VALUES ('test', 'test123');
INSERT INTO users (username, password) VALUES ('guest', 'guest123');
INSERT INTO users (username, password) VALUES ('admin\'--', 'hack');