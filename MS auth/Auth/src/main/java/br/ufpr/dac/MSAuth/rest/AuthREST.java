package br.ufpr.dac.MSAuth.rest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpr.dac.MSAuth.model.Login;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class AuthREST {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Login login) {
        Map<String, String> message = new HashMap<>();
        message.put("email", login.getLogin());
        message.put("password", login.getSenha());

        
        Object responseObj = rabbitTemplate.convertSendAndReceive("auth.request", message);
        if (responseObj instanceof Map) {
            Map<String, Object> response = (Map<String, Object>) responseObj;
            return ResponseEntity.ok(response);
            
        } else {
            System.out.println("Resposta inesperada: " + responseObj);
            return (ResponseEntity<Map<String, Object>>) ResponseEntity.status(401);
        }

    }
}
