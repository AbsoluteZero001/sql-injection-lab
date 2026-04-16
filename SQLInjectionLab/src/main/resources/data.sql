-- 清空旧数据（可选，确保每次启动数据一致）
DELETE FROM comments;
DELETE FROM users;
DELETE FROM articles;
DELETE FROM settings;

-- 初始化用户数据
INSERT INTO users (username, password, email, status, role, description) VALUES ('admin', 'admin123', 'admin@example.com', 'active', 'admin', 'Administrator account');
INSERT INTO users (username, password, email, status, role, description) VALUES ('test', 'test123', 'test@example.com', 'active', 'user', 'Test user account');
INSERT INTO users (username, password, email, status, role, description) VALUES ('guest', 'guest123', 'guest@example.com', 'inactive', 'user', 'Guest user account');
INSERT INTO users (username, password, email, status, role, description) VALUES ('admin\'--', 'hack', 'hack@example.com', 'active', 'user', 'Test account for SQL injection');

-- 初始化评论数据
INSERT INTO comments (user_id, content, status) VALUES (1, '这是一条正常的评论', 'approved');
INSERT INTO comments (user_id, content, status) VALUES (2, '这是另一条评论', 'pending');
INSERT INTO comments (user_id, content, status) VALUES (3, '<script>alert("XSS攻击")</script>', 'pending');

-- 初始化文章数据
INSERT INTO articles (title, content, author_id, status) VALUES ('欢迎使用文章管理系统', '这是一个示例文章，用于演示文章管理功能。', 1, 'published');
INSERT INTO articles (title, content, author_id, status) VALUES ('SQL注入学习指南', '这篇文章将教你如何识别和利用SQL注入漏洞。', 1, 'published');
INSERT INTO articles (title, content, author_id, status) VALUES ('XSS攻击防御', '了解跨站脚本攻击的原理和防御方法。', 2, 'draft');

-- 初始化系统设置
INSERT INTO settings (setting_key, setting_value, description) VALUES ('site_name', 'SQL注入实验室', '网站名称');
INSERT INTO settings (setting_key, setting_value, description) VALUES ('site_description', '一个用于学习SQL注入和XSS漏洞的教育平台', '网站描述');
INSERT INTO settings (setting_key, setting_value, description) VALUES ('comments_enabled', 'true', '是否启用评论功能');
INSERT INTO settings (setting_key, setting_value, description) VALUES ('registration_enabled', 'true', '是否开放用户注册');