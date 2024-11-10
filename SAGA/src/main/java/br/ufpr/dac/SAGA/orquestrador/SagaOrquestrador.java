package br.ufpr.dac.SAGA.orquestrador;

import java.util.*;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SagaOrquestrador {

    private static final Logger logger = LoggerFactory.getLogger(SagaOrquestrador.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    // This will handle the login request from the API Gateway
    public void startSaga(String email, String password) {
        // Step 1: Send message to MS Clientes to verify client
        Map<String, String> clientCheckMessage = new HashMap<>();
        clientCheckMessage.put("email", email);  // Password should not be part of client verification
        rabbitTemplate.convertAndSend("saga-exchange", "client.verification", clientCheckMessage);

        logger.info("Client verification request sent for email: {}", email);
    }

    @RabbitListener(queues = "client.verification.response")
    public void handleClientVerificationResponse(Map<String, Object> response) {
        boolean clientExists = (boolean) response.get("clientExists");

        if (clientExists) {
            // Step 2: If client exists, send authentication request to MS Auth
            String email = response.get("email").toString();
            String id = response.get("id").toString();  // Assuming the id might be useful later
            Map<String, String> authRequestMessage = new HashMap<>();
            authRequestMessage.put("email", email);
            rabbitTemplate.convertAndSend("saga-exchange", "auth.request", authRequestMessage);

            logger.info("Client exists. Proceeding with authentication for email: {}", email);
        } else {
            // Handle client not found scenario (respond to API Gateway)
            String errorMessage = (String) response.get("message");
            logger.error("Client verification failed: {}", errorMessage);
        }
    }

    @RabbitListener(queues = "auth.response")
    public void handleAuthResponse(Map<String, Object> response) {
        boolean authSuccess = (boolean) response.get("authSuccess");

        if (authSuccess) {
            // Respond back to API Gateway with authentication success and token
            String token = (String) response.get("token");
            logger.info("Authentication successful. Token: {}", token);
        } else {
            // Respond back to API Gateway with authentication failure
            String errorMessage = (String) response.get("errorMessage");
            logger.error("Authentication failed: {}", errorMessage);
        }
    }
}
