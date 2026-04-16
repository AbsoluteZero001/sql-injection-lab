package com.springboot.sqlinjectionlab.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 会话管理工具类
 * 用于管理用户登录状态和获取当前用户信息
 *
 * 注意：这个工具类是为了解决硬编码用户ID问题而创建的，
 * 但不修复SQL注入漏洞（这是教育目的）
 */
public class SessionUtil {

    public static final String USER_ID_KEY = "userId";
    public static final String USERNAME_KEY = "username";
    public static final String ROLE_KEY = "role";

    /**
     * 获取当前HTTP会话
     */
    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession();
    }

    /**
     * 获取当前登录用户的ID
     * @return 用户ID，如果未登录则返回null
     */
    public static Integer getCurrentUserId() {
        HttpSession session = getSession();
        Object userId = session.getAttribute(USER_ID_KEY);
        return userId != null ? (Integer) userId : null;
    }

    /**
     * 获取当前登录用户的用户名
     * @return 用户名，如果未登录则返回null
     */
    public static String getCurrentUsername() {
        HttpSession session = getSession();
        return (String) session.getAttribute(USERNAME_KEY);
    }

    /**
     * 获取当前登录用户的角色
     * @return 用户角色（user/admin），如果未登录则返回null
     */
    public static String getCurrentUserRole() {
        HttpSession session = getSession();
        return (String) session.getAttribute(ROLE_KEY);
    }

    /**
     * 检查当前用户是否是管理员
     * @return 如果是管理员返回true，否则返回false
     */
    public static boolean isAdmin() {
        return "admin".equals(getCurrentUserRole());
    }

    /**
     * 设置用户登录信息到会话中
     * @param userId 用户ID
     * @param username 用户名
     * @param role 用户角色
     */
    public static void setLoginUser(Integer userId, String username, String role) {
        HttpSession session = getSession();
        session.setAttribute(USER_ID_KEY, userId);
        session.setAttribute(USERNAME_KEY, username);
        session.setAttribute(ROLE_KEY, role);
    }

    /**
     * 清除用户登录信息（退出登录）
     */
    public static void clearLoginUser() {
        HttpSession session = getSession();
        session.removeAttribute(USER_ID_KEY);
        session.removeAttribute(USERNAME_KEY);
        session.removeAttribute(ROLE_KEY);
    }

    /**
     * 检查用户是否已登录
     * @return 如果已登录返回true，否则返回false
     */
    public static boolean isLoggedIn() {
        return getCurrentUserId() != null;
    }
}