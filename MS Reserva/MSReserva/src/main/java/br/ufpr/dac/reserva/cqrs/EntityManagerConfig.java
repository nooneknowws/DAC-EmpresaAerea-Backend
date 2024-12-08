package br.ufpr.dac.reserva.cqrs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class EntityManagerConfig {
	
	@Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
            new HibernateJpaVendorAdapter(), 
            new HashMap<>(), 
            null
        );
    }

    @Primary
    @Bean(name = "writeEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean writeEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("writeDataSource") DataSource writeDataSource) {
        
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        
        return builder
                .dataSource(writeDataSource)
                .packages("br.ufpr.dac.reserva.model")
                .properties(properties)
                .persistenceUnit("write")
                .build();
    }

    @Primary
    @Bean(name = "writeTransactionManager")
    PlatformTransactionManager writeTransactionManager(
            @Qualifier("writeEntityManagerFactory") EntityManagerFactory writeEntityManagerFactory) {
        return new JpaTransactionManager(writeEntityManagerFactory);
    }

    // Read EntityManagerFactory
    @Bean(name = "readEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean readEntityManagerFactory(
            EntityManagerFactoryBuilder builder, 
            @Qualifier("readDataSource") DataSource readDataSource) {
        
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");

        return builder
                .dataSource(readDataSource)
                .packages("br.ufpr.dac.reserva.model")
                .properties(properties)
                .persistenceUnit("read")
                .build();
    }

    @Bean(name = "readTransactionManager")
    PlatformTransactionManager readTransactionManager(
            @Qualifier("readEntityManagerFactory") EntityManagerFactory readEntityManagerFactory) {
        return new JpaTransactionManager(readEntityManagerFactory);
    }
}
