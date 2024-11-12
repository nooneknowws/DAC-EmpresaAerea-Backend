package br.ufpr.dac.MSClientes.rabbitmq;

import java.io.IOException;
import java.util.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;

import com.rabbitmq.client.Channel;

import br.ufpr.dac.MSClientes.models.dto.LoginDTO;
import br.ufpr.dac.MSClientes.services.ClienteService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RabbitListenerClientes {

    private static final Logger logger = LoggerFactory.getLogger(RabbitListenerClientes.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ClienteService clienteService;

    @RabbitListener(queues = "client.verification", ackMode = "MANUAL")
    public void verifyClient(LoginDTO loginDTO, Message message, Channel channel) {
        try {
            logger.info("Received client verification request for email: {}", loginDTO.getEmail());
            boolean isValid = clienteService.verificarCliente(loginDTO.getEmail(), loginDTO.getSenha());

            Map<String, String> response = new HashMap<>();
            response.put("email", loginDTO.getEmail());
            response.put("status", isValid ? "success" : "failure");
            response.put("message", isValid ? "Authentication successful" : "Invalid credentials");

            rabbitTemplate.convertAndSend("saga-exchange", "client.verification.response", response);
            logger.info("Sent client verification response to saga-exchange with routing key 'client.verification.response'");

            // Acknowledge the message after successful processing
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("Error verifying client", e);
            try {
                // Reject the message and do not requeue
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ioException) {
                logger.error("Error during message rejection", ioException);
            }
        }
    }
}