package ru.ilya.myProject.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class OracleDataSourceConfig {

    @Bean(name = "oracleHikariConfig")
    @ConfigurationProperties(prefix = "app.datasource.oracle.hikari")
    public HikariConfig oracleHikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = "oracleDataSource")
    public DataSource oracleDataSource(@Qualifier("oracleHikariConfig") HikariConfig config) {
        return new HikariDataSource(config);
    }

    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate oracleJdbcTemplate(@Qualifier("oracleDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "oracleTxManager")
    public DataSourceTransactionManager oracleTxManager(@Qualifier("oracleDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}