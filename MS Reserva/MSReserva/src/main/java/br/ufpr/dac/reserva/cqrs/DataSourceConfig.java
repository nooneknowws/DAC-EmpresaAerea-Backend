package br.ufpr.dac.reserva.cqrs;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "writeDataSource")
    DataSource writeDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/ms-reservas-write")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "readDataSource")
    DataSource readDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/ms-reservas-read")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
