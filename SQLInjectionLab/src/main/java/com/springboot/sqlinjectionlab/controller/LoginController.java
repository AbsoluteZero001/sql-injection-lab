package com.springboot.sqlinjectionlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam String username, @RequestParam String password) {
        // 直接拼接SQL语句，允许SQL注入攻击
        String sql = "SELECT COUNT(*) FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        if (count != null && count > 0) {
            // 登录成功
            return new RedirectView("/success");
        } else {
            // 登录失败，返回登录页面
            return new RedirectView("/");
        }
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }
}
