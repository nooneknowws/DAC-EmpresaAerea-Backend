package br.ufpr.dac.MSAuth.rest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.ufpr.dac.MSAuth.model.dto.LoginDTO;

@CrossOrigin
@RestController
public class AuthREST {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthREST.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        logger.info("Received login request for email: {}", loginDTO.getEmail());
        logger.info("Received login request for senha: {}", loginDTO.getSenha());
        rabbitTemplate.convertAndSend("saga-exchange", "auth.request", loginDTO);
        logger.info("Sent login request to saga-exchange with routing key 'auth.request'");
        return ResponseEntity.accepted().body("Login request in process");
    }
}
