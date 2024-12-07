package br.ufpr.dac.SAGA.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
//Verificações
    @Bean
    Queue clientVerificationQueue() {
    	return QueueBuilder.durable("client.verification")
                .ttl(30000)  // TTL of 1 minute
                .build();
    }

    @Bean
    Queue employeeVerificationQueue() {
        return new Queue("employee.verification");
    }

    @Bean
    Queue authRequestQueue() {
    	return QueueBuilder.durable("auth.request")
                .ttl(30000)  // TTL of 1 minute
                .build();
    }
    
//Respostas
    @Bean
    Queue authResponseQueue() {
    	return QueueBuilder.durable("auth.response")
                .ttl(30000)  // TTL of 1 minute
                .build();
    }
    @Bean
    Queue clientResponseQueue() {
        return QueueBuilder.durable("client.verification.response")
                .ttl(30000)  // TTL of 1 minute
                .build();
    }
    @Bean
    Queue employeeVerificationResponseQueue() {
        return new Queue("employee.verification.response");
    }


    @Bean
    DirectExchange exchange() {
        return new DirectExchange("saga-exchange");
    }
//Bindings
    @Bean
    Binding clientVerificationBinding(@Qualifier("clientVerificationQueue") Queue clientVerificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(clientVerificationQueue).to(exchange).with("client.verification");
    }

    @Bean
    Binding authRequestBinding(@Qualifier("authRequestQueue") Queue authRequestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(authRequestQueue).to(exchange).with("auth.request");
    }

    @Bean
    Binding authResponseBinding(@Qualifier("authResponseQueue") Queue authResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(authResponseQueue).to(exchange).with("auth.response");
    }
    @Bean
    Binding clientResponseBinding(@Qualifier("clientResponseQueue") Queue clientResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(clientResponseQueue).to(exchange).with("client.verification.response");
    }
    @Bean
    Binding employeeVerificationBinding(@Qualifier("employeeVerificationQueue") Queue employeeVerificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(employeeVerificationQueue).to(exchange).with("employee.verification");
    }

    @Bean
    Binding employeeVerificationResponseBinding(@Qualifier("employeeVerificationResponseQueue") Queue employeeVerificationResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(employeeVerificationResponseQueue).to(exchange).with("employee.verification.response");
    }

    MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(new DefaultJackson2JavaTypeMapper() {{
            setTrustedPackages("java.util", "br.ufpr.dac.MSAuth.model");
        }});
        return converter;
    }
    

    @Bean
    AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
