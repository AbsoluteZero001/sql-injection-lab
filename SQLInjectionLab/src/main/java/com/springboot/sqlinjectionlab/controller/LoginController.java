package com.springboot.sqlinjectionlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String loginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            // 检查用户名和密码是否正确，并获取用户信息
            String checkUserSql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(checkUserSql);

            if (users == null || users.isEmpty()) {
                // 用户不存在或密码错误
                model.addAttribute("error", "用户名或密码错误");
                return "login";
            }

            // 登录成功，根据角色跳转到不同页面
            Map<String, Object> user = users.get(0);
            String role = (String) user.get("role");
            if (role != null && role.equals("admin")) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/home";
            }
        } catch (Exception e) {
            // 捕获异常，返回错误信息用于 SQL 注入学习
            model.addAttribute("error", "查询异常: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }

    // 注册页面
    @GetMapping("/register")
    public String registerPage() {
        return "login";
    }

    // 处理注册
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam String email, 
                          @RequestParam String status, @RequestParam String role, @RequestParam String description, 
                          Model model) {
        try {
            // 检查用户名是否已存在
            String checkUserSql = "SELECT COUNT(*) as cnt FROM users WHERE username = '" + username + "'";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(checkUserSql);
            Integer userCount = ((Number) result.get(0).get("cnt")).intValue();
            
            if (userCount > 0) {
                // 用户名已存在
                model.addAttribute("registerError", "用户名已存在");
                return "login";
            }
            
            // 插入新用户
            String insertUserSql = "INSERT INTO users (username, password, email, status, role, description) " +
                                 "VALUES ('" + username + "', '" + password + "', '" + email + "', '" + status + "', '" + role + "', '" + description + "')";
            jdbcTemplate.execute(insertUserSql);
            
            // 注册成功，重定向到登录页面
            model.addAttribute("error", "注册成功，请登录");
            return "login";
        } catch (Exception e) {
            // 注册失败
            model.addAttribute("registerError", "注册失败：" + e.getMessage());
            return "login";
        }
    }

    // 用户首页
    @GetMapping("/user/home")
    public String userHome(Model model) {
        // 获取用户评论列表
        String sql = "SELECT c.id, c.content, c.status, c.created_at, u.username FROM comments c JOIN users u ON c.user_id = u.id";
        List<Map<String, Object>> comments = jdbcTemplate.queryForList(sql);
        model.addAttribute("comments", comments);
        return "user_home";
    }

    // 管理员首页
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin_dashboard";
    }

    // 评论审核页面（XSS注入测试页面）
    @GetMapping("/admin/comments")
    public String adminComments(Model model) {
        // 获取待审核的评论
        String sql = "SELECT c.id, c.content, c.status, c.created_at, u.username FROM comments c JOIN users u ON c.user_id = u.id WHERE c.status = 'pending'";
        List<Map<String, Object>> comments = jdbcTemplate.queryForList(sql);
        model.addAttribute("comments", comments);
        return "admin_comments";
    }

    // 提交评论
    @PostMapping("/user/addComment")
    public String addComment(@RequestParam String content, Model model) {
        // 这里简化处理，默认使用user_id=1
        String sql = "INSERT INTO comments (user_id, content, status) VALUES (1, '" + content + "', 'pending')";
        jdbcTemplate.execute(sql);
        return "redirect:/user/home";
    }

    // 审核评论
    @PostMapping("/admin/approveComment")
    public String approveComment(@RequestParam int id) {
        String sql = "UPDATE comments SET status = 'approved' WHERE id = " + id;
        jdbcTemplate.execute(sql);
        return "redirect:/admin/comments";
    }

    // 拒绝评论
    @PostMapping("/admin/rejectComment")
    public String rejectComment(@RequestParam int id) {
        String sql = "UPDATE comments SET status = 'rejected' WHERE id = " + id;
        jdbcTemplate.execute(sql);
        return "redirect:/admin/comments";
    }
}
