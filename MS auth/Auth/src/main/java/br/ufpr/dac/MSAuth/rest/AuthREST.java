package br.ufpr.dac.MSAuth.rest;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.ufpr.dac.MSAuth.model.dto.LoginDTO;
import br.ufpr.dac.MSAuth.service.JwtService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class AuthREST {

    private static final Logger logger = LoggerFactory.getLogger(AuthREST.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        logger.info("Received login request for email: {}", loginDTO.getEmail());

        rabbitTemplate.convertAndSend("saga-exchange", "auth.request", loginDTO);
        logger.info("Sent login request to saga-exchange with routing key 'auth.request'");

        String responseQueue = "auth.response";
        Message responseMessage = rabbitTemplate.receive(responseQueue, TimeUnit.SECONDS.toMillis(10));

        if (responseMessage != null) {
            @SuppressWarnings("unchecked")
			Map<String, String> response = (Map<String, String>) rabbitTemplate.getMessageConverter()
                    .fromMessage(responseMessage);
            logger.info("Received response from RabbitMQ: {}", response);

            if ("success".equals(response.get("status"))) {
                // Generate JWT token
            	String id = response.get("id");
                String email = response.get("email");
                String token = jwtService.generateToken(email);
                String perfil = response.get("perfil");
                Map<String, String> replyMessage = new HashMap<>();
                replyMessage.put("id", id);
                replyMessage.put("status", "success");
                replyMessage.put("token", token);
                replyMessage.put("email", email);
                replyMessage.put("perfil", perfil);
                return ResponseEntity.ok(replyMessage);
            } else {
                Map<String, String> errorMessage = new HashMap<>();
                errorMessage.put("status", "error");
                errorMessage.put("message", response.get("message"));
                return ResponseEntity.status(401).body(errorMessage);
            }
        } else {
            logger.warn("No response from RabbitMQ within the timeout period");
            return ResponseEntity.status(504).body("No response from authentication service");
        }
    }
}
