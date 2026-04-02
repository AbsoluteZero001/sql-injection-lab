package com.springboot.sqlinjectionlab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SqlInjectionLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlInjectionLabApplication.class, args);
	}

	/**
	 * 应用启动后验证数据库初始化
	 */
	@Bean
	public CommandLineRunner verifyDatabaseInit(JdbcTemplate jdbcTemplate) {
		return args -> {
			System.out.println("========================================");
			System.out.println("✅ 数据库初始化验证开始...");
			
			try {
				// 检查 users 表是否存在
				String checkTableSql = "SELECT COUNT(*) FROM information_schema.tables " +
						"WHERE table_schema = DATABASE() AND table_name = 'users'";
				int tableCount = jdbcTemplate.queryForObject(checkTableSql, Integer.class);
				
				if (tableCount > 0) {
					System.out.println("✅ users 表已存在");
					
					// 查询所有用户
					String sql = "SELECT * FROM users";
					List<Map<String, Object>> users = jdbcTemplate.queryForList(sql);
					System.out.println("✅ 成功加载 " + users.size() + " 个用户:");
					
					for (Map<String, Object> user : users) {
						System.out.println("   - ID: " + user.get("id") + 
							", Username: " + user.get("username") + 
							", Password: " + user.get("password"));
					}
				} else {
					System.err.println("❌ users 表不存在！");
				}
				
			} catch (Exception e) {
				System.err.println("❌ 数据库验证失败：" + e.getMessage());
				e.printStackTrace();
			}
			
			System.out.println("========================================");
		};
	}

}
