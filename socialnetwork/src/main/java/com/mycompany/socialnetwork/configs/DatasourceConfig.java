package com.mycompany.socialnetwork.configs;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DatasourceConfig {

    public DataSource dataSource() {
        final var datasource = new DriverManagerDataSource();
        datasource.setDriverClassName("org.h2.Driver");
        datasource.setUrl("jdbc:h2:mem:testdb");
        datasource.setUsername("sa");
        datasource.setPassword("password");

        return datasource;
    }
}
