package br.ufpr.dac.SAGA.orquestrador;

import java.io.IOException;
import java.util.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import br.ufpr.dac.SAGA.models.dto.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SagaOrquestrador {

    private static final Logger logger = LoggerFactory.getLogger(SagaOrquestrador.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @RabbitListener(queues = "auth.request", ackMode = "MANUAL")
    public void handleAuthSaga(LoginDTO loginDTO, Message message, Channel channel) {
        try {
            logger.info("Received login request from 'auth.request' queue for email: {}", loginDTO.getEmail());
            rabbitTemplate.convertAndSend("saga-exchange", "client.verification", loginDTO);
            logger.info("Sent verification request to saga-exchange with routing key 'client.verification'");

            
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("Error handling auth saga", e);
            try {
                
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ioException) {
                logger.error("Error during message rejection", ioException);
            }
        }
    }

    @RabbitListener(queues = "client.verification.response", ackMode = "MANUAL")
    public void handleClientVerificationResponse(Map<String, String> response, Message message, Channel channel) {
        try {
            logger.info("Received client verification response for email: {} with status: {}", response.get("email"), response.get("status"));
            rabbitTemplate.convertAndSend("saga-exchange", "auth.response", response);
            logger.info("Sent response to saga-exchange with routing key 'auth.response'");

            
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("Error handling client verification response", e);
            try {
                
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ioException) {
                logger.error("Error during message rejection", ioException);
            }
        }
    }
}
