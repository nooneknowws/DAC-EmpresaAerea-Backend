package br.ufpr.dac.MSAuth.rabbit;

import br.ufpr.dac.MSAuth.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.*;

@Service
public class RabbitListenerAuth {

    private static final Logger logger = LoggerFactory.getLogger(RabbitListenerAuth.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AuthService userService;  // Inject a UserService to handle authentication

    @RabbitListener(queues = "auth.request")
    public void receiveMessage(Map<String, String> message) {
        String email = message.get("email");
        String password = message.get("password");

        logger.info("Received authentication request for user: {}", email);

        // Perform the actual authentication (e.g., check if the user exists and verify password)
        boolean isAuthenticated = userService.authenticate(email, password);

        AuthResponse response = new AuthResponse();
        response.setAuthSuccess(isAuthenticated);

        if (isAuthenticated) {
            // Generate a JWT token with expiration and claims
            String token = jwtService.generateToken(email);
            response.setToken(token);
            logger.info("Authentication successful for user: {}", email);
        } else {
            // Handle authentication failure
            response.setErrorMessage("Invalid credentials");
            logger.warn("Authentication failed for user: {}", email);
        }

        // Send the authentication response back to the saga orchestration service
        rabbitTemplate.convertAndSend("saga-exchange", "auth.response", response);
    }
}
