package br.ufpr.dac.MSClientes.rabbitmq;

import java.util.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import br.ufpr.dac.MSClientes.models.*;
import br.ufpr.dac.MSClientes.repository.ClienteRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RabbitListenerClientes {

    private static final Logger logger = LoggerFactory.getLogger(RabbitListenerClientes.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ClienteRepo clienteRepository;

    @RabbitListener(queues = "client.verification")
    public void receiveMessage(Map<String, String> message) {
        String email = message.get("email");

        Optional<Usuario> optionalUsuario = clienteRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("email", email);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            response.put("clientExists", true);
            response.put("id", usuario.getId());  // Include id if needed
            logger.info("Client verification successful for email: {}", email);
        } else {
            response.put("clientExists", false);
            response.put("message", "Client not found");
            logger.error("Client not found for email: {}", email);
        }

        rabbitTemplate.convertAndSend("saga-exchange", "client.verification.response", response);
    }
}
