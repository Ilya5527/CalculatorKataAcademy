package ru.ilya.myProject.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class PostgresDataSourceConfig {

    @Bean(name = "postgresHikariConfig")
    @ConfigurationProperties(prefix = "app.datasource.postgres.hikari")
    public HikariConfig postgresHikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = "postgresDataSource")
    @Primary
    public DataSource postgresDataSource(@Qualifier("postgresHikariConfig") HikariConfig config) {
        return new HikariDataSource(config);
    }

    @Bean(name = "postgresJdbcTemplate")
    public JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "postgresTxManager")
    public DataSourceTransactionManager postgresTxManager(@Qualifier("postgresDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}