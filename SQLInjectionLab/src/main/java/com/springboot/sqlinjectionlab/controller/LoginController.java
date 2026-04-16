package com.springboot.sqlinjectionlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.sqlinjectionlab.util.SessionUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

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
            String checkUserSql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password
                    + "'";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(checkUserSql);

            if (users == null || users.isEmpty()) {
                // 用户不存在或密码错误
                model.addAttribute("error", "用户名或密码错误");
                return "login";
            }

            // 登录成功，存储用户信息到会话中
            Map<String, Object> user = users.get(0);
            Integer userId = ((Number) user.get("id")).intValue();
            String dbUsername = (String) user.get("username");
            String role = (String) user.get("role");

            // 存储到会话
            SessionUtil.setLoginUser(userId, dbUsername, role);

            // 根据角色跳转到不同页面
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
        return "register";
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
                    "VALUES ('" + username + "', '" + password + "', '" + email + "', '" + status + "', '" + role
                    + "', '" + description + "')";
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
        Integer userId = SessionUtil.getCurrentUserId();
        if (userId == null) {
            return "redirect:/?error=请先登录";
        }

        // 获取用户信息
        String getUserSql = "SELECT * FROM users WHERE id = " + userId;
        Map<String, Object> user = jdbcTemplate.queryForMap(getUserSql);
        model.addAttribute("user", user);

        // 获取用户评论列表
        String sql = "SELECT c.id, c.content, c.status, c.created_at, u.username, u.avatar FROM comments c JOIN users u ON c.user_id = u.id";
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
        Integer userId = SessionUtil.getCurrentUserId();
        if (userId == null) {
            return "redirect:/?error=请先登录";
        }
        // 使用当前用户ID，保留SQL注入漏洞用于教育目的
        String sql = "INSERT INTO comments (user_id, content, status) VALUES (" + userId + ", '" + content + "', 'pending')";
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

    // 获取个人资料
    @GetMapping("/user/profile")
    public String userProfile(Model model) {
        Integer userId = SessionUtil.getCurrentUserId();
        if (userId == null) {
            return "redirect:/?error=请先登录";
        }
        // 获取当前登录用户的资料
        String sql = "SELECT * FROM users WHERE id = " + userId;
        Map<String, Object> user = jdbcTemplate.queryForMap(sql);
        model.addAttribute("user", user);
        return "user_profile";
    }

    // 更新个人资料
    @PostMapping("/user/updateProfile")
    public String updateProfile(@RequestParam String username, @RequestParam String email,
            @RequestParam String password, @RequestParam String status,
            @RequestParam String description, @RequestParam MultipartFile avatar,
            Model model) {
        Integer userId = SessionUtil.getCurrentUserId();
        if (userId == null) {
            return "redirect:/?error=请先登录";
        }

        try {
            // 获取当前用户的旧头像文件名
            String getOldAvatarSql = "SELECT avatar FROM users WHERE id = " + userId;
            Map<String, Object> oldUser = jdbcTemplate.queryForMap(getOldAvatarSql);
            String oldAvatar = (String) oldUser.get("avatar");

            // 使用当前用户ID更新资料
            StringBuilder sql = new StringBuilder("UPDATE users SET username = '" + username + "', email = '" + email
                    + "', status = '" + status + "', description = '" + description + "'");

            if (password != null && !password.isEmpty()) {
                sql.append(", password = '" + password + "'");
            }

            // 处理头像上传
            if (!avatar.isEmpty()) {
                // 验证头像文件
                String validationError = validateAvatarFile(avatar);
                if (validationError != null) {
                    // 验证失败，返回错误信息
                    String getUserSql = "SELECT * FROM users WHERE id = " + userId;
                    Map<String, Object> user = jdbcTemplate.queryForMap(getUserSql);
                    model.addAttribute("user", user);
                    model.addAttribute("error", validationError);
                    return "user_profile";
                }

                // 创建上传目录
                String uploadDir = "uploads";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // 生成唯一文件名
                String originalFilename = avatar.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                // 只保留安全文件扩展名，防止路径遍历攻击
                if (!fileExtension.matches("(?i)\\.(jpg|jpeg|png|gif)")) {
                    fileExtension = ".jpg";
                }
                String fileName = UUID.randomUUID().toString() + fileExtension;
                String filePath = uploadDir + File.separator + fileName;

                // 保存文件
                avatar.transferTo(new File(filePath));

                // 更新头像路径
                sql.append(", avatar = '" + fileName + "'");

                // 删除旧头像文件（如果不是默认头像）
                if (oldAvatar != null && !oldAvatar.equals("default.png")) {
                    File oldAvatarFile = new File(uploadDir + File.separator + oldAvatar);
                    if (oldAvatarFile.exists()) {
                        oldAvatarFile.delete();
                    }
                }
            }

            sql.append(" WHERE id = " + userId);
            jdbcTemplate.execute(sql.toString());

            // 重新获取用户资料
            String getUserSql = "SELECT * FROM users WHERE id = " + userId;
            Map<String, Object> user = jdbcTemplate.queryForMap(getUserSql);
            model.addAttribute("user", user);
            model.addAttribute("success", "个人资料更新成功");

            return "user_profile";
        } catch (Exception e) {
            // 更新失败
            String getUserSql = "SELECT * FROM users WHERE id = " + userId;
            Map<String, Object> user = jdbcTemplate.queryForMap(getUserSql);
            model.addAttribute("user", user);
            model.addAttribute("error", "更新失败：" + e.getMessage());
            return "user_profile";
        }
    }

    // 我的评论页面
    @GetMapping("/user/comments")
    public String userComments(Model model) {
        Integer userId = SessionUtil.getCurrentUserId();
        if (userId == null) {
            return "redirect:/?error=请先登录";
        }
        // 获取当前用户的评论
        String sql = "SELECT c.id, c.content, c.status, c.created_at, u.username FROM comments c JOIN users u ON c.user_id = u.id WHERE c.user_id = " + userId;
        List<Map<String, Object>> comments = jdbcTemplate.queryForList(sql);
        model.addAttribute("comments", comments);
        return "user_comments";
    }

    // 验证头像文件
    private String validateAvatarFile(MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return "请选择头像文件";
        }

        // 检查文件大小（限制为2MB）
        long maxSize = 2 * 1024 * 1024; // 2MB
        if (file.getSize() > maxSize) {
            return "头像文件大小不能超过2MB";
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "无效的文件名";
        }

        String fileExtension = "";
        if (originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        // 允许的文件扩展名
        if (!fileExtension.matches("\\.(jpg|jpeg|png|gif)")) {
            return "只支持JPG、JPEG、PNG、GIF格式的图片";
        }

        // 检查文件内容类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return "文件必须是图片格式";
        }

        // 验证图片尺寸
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                return "无法读取图片文件，可能不是有效的图片格式";
            }

            int width = image.getWidth();
            int height = image.getHeight();

            // 最小尺寸限制（避免上传过小的图片）
            if (width < 50 || height < 50) {
                return "图片尺寸过小，最小尺寸为50x50像素";
            }

            // 最大尺寸限制（避免上传过大的图片）
            if (width > 2000 || height > 2000) {
                return "图片尺寸过大，最大尺寸为2000x2000像素";
            }

            // 建议宽高比接近正方形（可选）
            double ratio = (double) width / height;
            if (ratio < 0.5 || ratio > 2.0) {
                // 只是警告，不是错误
                // 可以记录日志或忽略
                // 这里不阻止上传，只是宽高比不理想
            }
        } catch (IOException e) {
            // 读取图片失败，可能是损坏的图片文件
            return "无法验证图片文件，请确保文件完整且格式正确";
        }

        return null; // 验证通过
    }

}
