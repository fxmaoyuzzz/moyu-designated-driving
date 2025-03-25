//package com.moyu.daijia.system.config;
//
//
//import com.zaxxer.hikari.HikariDataSource;
//import io.seata.rm.datasource.DataSourceProxy;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
///**
// * 数据源配置
// *
// * https://seata.io/zh-cn/docs/ops/deploy-guide-beginner.html
// *
// * @author HelloWoodes
// */
//@Configuration
//public class DataSourceConfig {
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driveClassName;
//
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public HikariDataSource hikariDataSource() {
//        HikariDataSource hikariDataSource = new HikariDataSource();
//        hikariDataSource.setUsername(username);
//        hikariDataSource.setPassword(password);
//        hikariDataSource.setJdbcUrl(url);
//        hikariDataSource.setDriverClassName(driveClassName);
//        return hikariDataSource;
//    }
//
//    /**
//     * 需要将 DataSourceProxy 设置为主数据源，否则事务无法回滚
//     *
//     * @param hikariDataSource The HikariDataSource
//     * @return The default datasource
//     */
//    @Primary
//    @Bean("dataSource")
//    public DataSourceProxy dataSourceProxy(HikariDataSource hikariDataSource) {
//        return new DataSourceProxy(hikariDataSource);
//    }
//
//}
