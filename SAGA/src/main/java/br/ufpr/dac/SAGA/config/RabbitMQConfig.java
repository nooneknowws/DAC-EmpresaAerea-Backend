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

    @Bean
    Queue clientVerificationQueue() {
    	return QueueBuilder.durable("client.verification")
                .ttl(30000)  // TTL of 1 minute
                .build();
    }

    @Bean
    Queue authRequestQueue() {
    	return QueueBuilder.durable("auth.request")
                .ttl(30000)  // TTL of 1 minute
                .build();
    }

    @Bean
    Queue authResponseQueue() {
    	return QueueBuilder.durable("auth.response")
                .ttl(30000)  // TTL of 1 minute
                .build();
    }

    @Bean
    Queue authReplyQueue() {
    	return QueueBuilder.durable("auth.reply")
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
    DirectExchange exchange() {
        return new DirectExchange("saga-exchange");
    }

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
    Binding authReplyBinding(@Qualifier("authReplyQueue") Queue authReplyQueue, DirectExchange exchange) {
        return BindingBuilder.bind(authReplyQueue).to(exchange).with("auth.reply");
    }
    @Bean
    Binding clientResponseBinding(@Qualifier("clientResponseQueue") Queue clientResponseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(clientResponseQueue).to(exchange).with("client.verification.response");
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
