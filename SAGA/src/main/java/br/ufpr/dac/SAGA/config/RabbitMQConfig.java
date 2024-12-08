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
    // Common configuration values
    private static final int QUEUE_TTL = 30000; // 30 seconds TTL

    /*
     * Authentication Related Queues
     * These queues handle the existing authentication flow
     */
    @Bean
    Queue authRequestQueue() {
        return QueueBuilder.durable("auth.request")
                .ttl(QUEUE_TTL)
                .build();
    }
    
    @Bean
    Queue authResponseQueue() {
        return QueueBuilder.durable("auth.response")
                .ttl(QUEUE_TTL)
                .build();
    }

    @Bean
    Queue clientVerificationQueue() {
        return QueueBuilder.durable("client.verification")
                .ttl(QUEUE_TTL)
                .build();
    }

    @Bean
    Queue employeeVerificationQueue() {
        return new Queue("employee.verification");
    }

    @Bean
    Queue clientResponseQueue() {
        return QueueBuilder.durable("client.verification.response")
                .ttl(QUEUE_TTL)
                .build();
    }

    @Bean
    Queue employeeVerificationResponseQueue() {
        return new Queue("employee.verification.response");
    }

    /*
     * Email Verification Related Queues
     * These queues handle the new email verification flow
     */
    @Bean
    Queue registrationRequestQueue() {
    	return QueueBuilder.durable("registration.request")
    			.ttl(QUEUE_TTL)
    			.build();
    }
    @Bean
    Queue clientRegistrationResponseQueue() {
    	return QueueBuilder.durable("client.registration.response")
    			.ttl(QUEUE_TTL)
    			.build();
    }
    
    @Bean
    Queue employeeRegistrationResponseQueue() {
    	return QueueBuilder.durable("employee.registration.response")
    			.ttl(QUEUE_TTL)
    			.build();
    }
    
    @Bean
    Queue emailVerificationRequestQueue() {
        return QueueBuilder.durable("email.verification.request")
                .ttl(QUEUE_TTL)
                .build();
    }

    @Bean
    Queue clientEmailCheckQueue() {
        return QueueBuilder.durable("client.email.check")
                .ttl(QUEUE_TTL)
                .build();
    }

    @Bean
    Queue employeeEmailCheckQueue() {
        return QueueBuilder.durable("employee.email.check")
                .ttl(QUEUE_TTL)
                .build();
    }

    @Bean
    Queue clientEmailCheckResponseQueue() {
        return QueueBuilder.durable("client.email.check.response")
                .ttl(QUEUE_TTL)
                .build();
    }

    @Bean
    Queue employeeEmailCheckResponseQueue() {
        return QueueBuilder.durable("employee.email.check.response")
                .ttl(QUEUE_TTL)
                .build();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("saga-exchange");
    }

    /*
     * Authentication Flow Bindings
     */
    @Bean
    Binding authRequestBinding(@Qualifier("authRequestQueue") Queue authRequestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(authRequestQueue).to(exchange).with("auth.request");
    }

    @Bean
    Binding authResponseBinding(@Qualifier("authResponseQueue") Queue authResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(authResponseQueue).to(exchange).with("auth.response");
    }
    
    @Bean
    Binding clientRegistrationResponseBinding(@Qualifier("clientRegistrationResponseQueue") Queue clientRegistrationResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(clientRegistrationResponseQueue).to(exchange).with("client.registration.response");
    }
    
    @Bean
    Binding employeeRegistrationResponseBinding(@Qualifier("employeeRegistrationResponseQueue") Queue employeeRegistrationResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(employeeRegistrationResponseQueue).to(exchange).with("employee.registration.response");
    }
    
    @Bean
    Binding clientVerificationBinding(@Qualifier("clientVerificationQueue") Queue clientVerificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(clientVerificationQueue).to(exchange).with("client.verification");
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

    /*
     * Email Verification Flow Bindings
     */
    @Bean
    Binding registrationRequestBinding(@Qualifier("registrationRequestQueue") Queue registrationRequestQueue, DirectExchange exchange) {
    	return BindingBuilder.bind(registrationRequestQueue).to(exchange).with("registration.request");
    }
    
    @Bean
    Binding emailVerificationRequestBinding(@Qualifier("emailVerificationRequestQueue") Queue emailVerificationRequestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(emailVerificationRequestQueue).to(exchange).with("email.verification.request");
    }

    @Bean
    Binding clientEmailCheckBinding(@Qualifier("clientEmailCheckQueue") Queue clientEmailCheckQueue, DirectExchange exchange) {
        return BindingBuilder.bind(clientEmailCheckQueue).to(exchange).with("client.email.check");
    }

    @Bean
    Binding employeeEmailCheckBinding(@Qualifier("employeeEmailCheckQueue") Queue employeeEmailCheckQueue, DirectExchange exchange) {
        return BindingBuilder.bind(employeeEmailCheckQueue).to(exchange).with("employee.email.check");
    }

    @Bean
    Binding clientEmailCheckResponseBinding(@Qualifier("clientEmailCheckResponseQueue") Queue clientEmailCheckResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(clientEmailCheckResponseQueue).to(exchange).with("client.email.check.response");
    }

    @Bean
    Binding employeeEmailCheckResponseBinding(@Qualifier("employeeEmailCheckResponseQueue") Queue employeeEmailCheckResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(employeeEmailCheckResponseQueue).to(exchange).with("employee.email.check.response");
    }

    /*
     * Message Converter Configuration
     */
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