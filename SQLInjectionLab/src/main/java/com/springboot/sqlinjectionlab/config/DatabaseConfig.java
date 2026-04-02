package com.springboot.sqlinjectionlab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * 数据库初始化配置（可选，Spring Boot 已自动处理）
 */
@Configuration
public class DatabaseConfig {

    /**
     * Spring Boot 会自动执行 schema.sql 和 data.sql
     * 如果需要更复杂的初始化逻辑，可以使用此方法
     */
    // @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
            new org.springframework.core.io.ClassPathResource("schema.sql"),
            new org.springframework.core.io.ClassPathResource("data.sql")
        );
        populator.setContinueOnError(false);
        populator.setSeparator(";");
        
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
