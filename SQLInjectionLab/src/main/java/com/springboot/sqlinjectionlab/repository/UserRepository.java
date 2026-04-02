package com.springboot.sqlinjectionlab.repository;

import com.springboot.sqlinjectionlab.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问层（使用 JdbcTemplate - 故意存在 SQL 注入风险用于教学）
 */
@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据用户名查询用户 - 存在 SQL 注入漏洞（用于教学）
     * ⚠️ 注意：这是故意不安全的代码，用于演示 SQL 注入攻击
     */
    public List<User> findByUsername(String username) {
        // ❌ 错误示范：字符串拼接 SQL（存在 SQL 注入风险）
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        
        // 使用 execute 方法执行静态 SQL（不安全的方式）
        return jdbcTemplate.execute((java.sql.Connection conn) -> {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            
            List<User> users = new java.util.ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
            
            rs.close();
            stmt.close();
            return users;
        });
    }

    /**
     * 安全的方式：使用参数化查询（防止 SQL 注入）
     * ✅ 正确示范：使用 PreparedStatement
     */
    public List<User> findByUsernameSafe(String username) {
        // ✅ 正确方式：使用参数化查询
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }, username);
    }

    /**
     * 获取所有用户
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }
}
