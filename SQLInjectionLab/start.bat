@echo off
chcp 65001 >nul
echo ==========================================
echo    SQL 注入实验室 - 启动中...
echo ==========================================
echo.
echo 🚀 正在启动 Spring Boot 应用...
echo.
echo 💡 提示：
echo    - 应用将在 http://localhost:8080/ 启动
echo    - 浏览器将自动打开登录页面
echo    - 如果未自动打开，请手动访问上述地址
echo.
echo ==========================================
echo.

call mvnw spring-boot:run

echo.
echo ==========================================
echo    应用已停止
echo ==========================================
pause
