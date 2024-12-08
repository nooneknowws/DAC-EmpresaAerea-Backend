package br.ufpr.dac.reserva.cqrs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.write.url}")
    private String writeUrl;
    @Value("${spring.datasource.write.username}")
    private String writeUsername;
    @Value("${spring.datasource.write.password}")
    private String writePassword;
    @Value("${spring.datasource.write.driver-class-name}")
    private String writeDriverClassName;

    @Value("${spring.datasource.read.url}")
    private String readUrl;
    @Value("${spring.datasource.read.username}")
    private String readUsername;
    @Value("${spring.datasource.read.password}")
    private String readPassword;
    @Value("${spring.datasource.read.driver-class-name}")
    private String readDriverClassName;

    @Primary
    @Bean(name = "writeDataSource")
    DataSource writeDataSource() {
        return DataSourceBuilder.create()
                .url(writeUrl)
                .username(writeUsername)
                .password(writePassword)
                .driverClassName(writeDriverClassName)
                .build();
    }

    @Bean(name = "readDataSource")
    DataSource readDataSource() {
        return DataSourceBuilder.create()
                .url(readUrl)
                .username(readUsername)
                .password(readPassword)
                .driverClassName(readDriverClassName)
                .build();
    }
}