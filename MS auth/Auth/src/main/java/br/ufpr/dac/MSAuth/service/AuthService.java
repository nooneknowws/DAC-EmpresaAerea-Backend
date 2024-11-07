package br.ufpr.dac.MSAuth.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public boolean authenticate(String email, String password) {
        Map<String, String> clientCheckMessage = new HashMap<>();
        clientCheckMessage.put("email", email);
        rabbitTemplate.convertAndSend("saga-exchange", "client.verification", clientCheckMessage);

        logger.info("Sent client verification request for email: {}", email);
		return false;
    }
}
