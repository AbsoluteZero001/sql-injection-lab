package com.springboot.sqlinjectionlab.controller;

import com.springboot.sqlinjectionlab.entity.User;
import com.springboot.sqlinjectionlab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有用户（用于测试数据库初始化）
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 根据用户名查询用户（不安全的实现 - 存在 SQL 注入）
     * ⚠️ 仅用于教学演示
     */
    @GetMapping("/unsafe/{username}")
    public List<User> getUserUnsafe(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据用户名查询用户（安全的实现 - 使用参数化查询）
     * ✅ 推荐方式
     */
    @GetMapping("/{username}")
    public List<User> getUserSafe(@PathVariable String username) {
        return userRepository.findByUsernameSafe(username);
    }
}
