package com.springboot.sqlinjectionlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        // 检查用户名是否存在
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = '" + username + "'";
        Integer userCount = jdbcTemplate.queryForObject(checkUserSql, Integer.class);
        
        if (userCount == null || userCount == 0) {
            // 用户不存在
            model.addAttribute("error", "用户不存在");
            return "login";
        }
        
        // 检查密码是否正确
        String checkPasswordSql = "SELECT COUNT(*) FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        Integer passwordCount = jdbcTemplate.queryForObject(checkPasswordSql, Integer.class);
        
        if (passwordCount != null && passwordCount > 0) {
            // 登录成功
            return "redirect:/success";
        } else {
            // 密码错误
            model.addAttribute("error", "密码错误");
            return "login";
        }
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }
}
