package com.funcionarios.funcionarios.rabbitmq;

import java.io.IOException;
import java.util.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;

import com.funcionarios.funcionarios.models.dto.LoginDTO;
import com.funcionarios.funcionarios.services.FuncionarioService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;


@Service
public class RabbitListenerFuncionarios {
  private static final Logger logger = LoggerFactory.getLogger(RabbitListenerFuncionarios.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FuncionarioService funcionarioService;

    @RabbitListener(queues = "funcionario.verification", ackMode = "MANUAL")
    public void verifyFuncionario(LoginDTO loginDTO, Message message, Channel channel) {
        try {
            logger.info("Received funcionario verification request for email: {}", loginDTO.getEmail());
           
            LoginDTO funcionarioInfo = funcionarioService.verificarFuncionario(loginDTO.getEmail(), loginDTO.getSenha());
            
            if (funcionarioInfo == null) {
                logger.info("Invalid credentials for email: {}", loginDTO.getEmail());
                sendVerificationResponse("failure", "Invalid credentials", null, message, channel);
                return;
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(funcionarioInfo.getId())); 
            response.put("email", funcionarioInfo.getEmail());           
            response.put("perfil", funcionarioInfo.getPerfil());         
            response.put("status", "success");
            response.put("message", "Authentication successful");

            rabbitTemplate.convertAndSend("saga-exchange", "funcionario.verification.response", response);
            logger.info("Sent funcionario verification response to saga-exchange with routing key 'funcionario.verification.response'");

            
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("Error verifying funcionario", e);
            try {
               
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ioException) {
                logger.error("Error during message rejection", ioException);
            }
        }
    }

    private void sendVerificationResponse(String status, String message, LoginDTO loginDTO, Message messageObj, Channel channel) {
        Map<String, String> response = new HashMap<>();
        if (loginDTO != null) {
            response.put("id", String.valueOf(loginDTO.getId()));
            response.put("email", loginDTO.getEmail());
            response.put("perfil", loginDTO.getPerfil());
        }
        response.put("status", status);
        response.put("message", message);
        
        rabbitTemplate.convertAndSend("saga-exchange", "funcionario.verification.response", response);
        logger.info("Sent funcionario verification response to saga-exchange with routing key 'funcionario.verification.response'");
        try {
            channel.basicAck(messageObj.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("Error during message acknowledgment", e);
        }
    }
}
