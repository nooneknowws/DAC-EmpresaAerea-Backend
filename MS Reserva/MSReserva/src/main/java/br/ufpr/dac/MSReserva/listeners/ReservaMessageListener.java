package br.ufpr.dac.MSReserva.listeners;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import br.ufpr.dac.MSReserva.cqrs.command.ReservaCommandService;
import br.ufpr.dac.MSReserva.model.Reserva;
import br.ufpr.dac.MSReserva.model.StatusReserva;
import jakarta.transaction.Transactional;

@Component
public class ReservaMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(ReservaMessageListener.class);

    @Autowired
    private ReservaCommandService commandService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

   

    @Transactional
    @RabbitListener(queues = "reserva.cancellation.complete")
    public void handleCancellationComplete(Message message, Channel channel) {
        try {
            Map<String, Object> result = objectMapper.readValue(message.getBody(), Map.class);
            Long reservaId = Long.valueOf(result.get("reservaId").toString());
            
            logger.info("Received cancellation completion notification for reserva: {}", reservaId);
            
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("Error processing cancellation completion", e);
            handleMessageError(channel, message);
        }
    }
    
    @RabbitListener(queues = "reserva.cancelamento.response")
    public void handleCancellationResponse(Map<String, Object> response) {
        Long reservaId = (Long) response.get("reservaId");
        String status = (String) response.get("status");
        
        if ("failure".equals(status)) {
            // Handle failure case
            logger.error("Cancellation failed for reserva: {}", reservaId);
        } else {
            logger.info("Cancellation completed successfully for reserva: {}", reservaId);
        }
    }

    private void sendErrorResponse(Long reservaId, String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("reservaId", reservaId);
        errorResponse.put("status", "error");
        errorResponse.put("message", errorMessage);

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "reserva.status.response",
            errorResponse
        );
    }

    private void handleMessageError(Channel channel, Message message) {
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } catch (IOException ex) {
            logger.error("Error during message rejection", ex);
        }
    }
}