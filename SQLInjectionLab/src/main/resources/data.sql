-- 清空旧数据（可选，确保每次启动数据一致）
DELETE FROM comments;
DELETE FROM users;

-- 初始化用户数据
INSERT INTO users (username, password, email, status, role, description) VALUES ('admin', 'admin123', 'admin@example.com', 'active', 'admin', 'Administrator account');
INSERT INTO users (username, password, email, status, role, description) VALUES ('test', 'test123', 'test@example.com', 'active', 'user', 'Test user account');
INSERT INTO users (username, password, email, status, role, description) VALUES ('guest', 'guest123', 'guest@example.com', 'inactive', 'user', 'Guest user account');
INSERT INTO users (username, password, email, status, role, description) VALUES ('admin\'--', 'hack', 'hack@example.com', 'active', 'user', 'Test account for SQL injection');

-- 初始化评论数据
INSERT INTO comments (user_id, content, status) VALUES (1, '这是一条正常的评论', 'approved');
INSERT INTO comments (user_id, content, status) VALUES (2, '这是另一条评论', 'pending');
INSERT INTO comments (user_id, content, status) VALUES (3, '<script>alert("XSS攻击")</script>', 'pending');