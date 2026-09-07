package com.springboot.sqlinjectionlab.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 创建数据库内部使用的动态 SQL 存储过程。
 * 应用层只执行 CALL，参数拼接发生在 MySQL 存储过程中，模拟真实业务里“SQL 不在代码中”的注入形态。
 */
@Component
public class LabProcedureBootstrap implements ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        createSearchProcedure();
        createUserLookupProcedure();
    }

    private void createSearchProcedure() {
        jdbcTemplate.execute("DROP PROCEDURE IF EXISTS lab_search_users");
        jdbcTemplate.execute("""
                CREATE PROCEDURE lab_search_users(IN p_keyword VARCHAR(255))
                BEGIN
                    SET @lab_sql = CONCAT(
                        "SELECT id, username, email, role, description FROM users WHERE username LIKE '%",
                        p_keyword,
                        "%'"
                    );
                    PREPARE lab_stmt FROM @lab_sql;
                    EXECUTE lab_stmt;
                    DEALLOCATE PREPARE lab_stmt;
                END""");
    }

    private void createUserLookupProcedure() {
        jdbcTemplate.execute("DROP PROCEDURE IF EXISTS lab_user_by_id");
        jdbcTemplate.execute("""
                CREATE PROCEDURE lab_user_by_id(IN p_id VARCHAR(255))
                BEGIN
                    SET @lab_sql = CONCAT(
                        "SELECT id, username, role, description FROM users WHERE id = ",
                        p_id
                    );
                    PREPARE lab_stmt FROM @lab_sql;
                    EXECUTE lab_stmt;
                    DEALLOCATE PREPARE lab_stmt;
                END""");
    }
}
