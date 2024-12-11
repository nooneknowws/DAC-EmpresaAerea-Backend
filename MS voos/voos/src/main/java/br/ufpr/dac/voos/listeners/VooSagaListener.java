package br.ufpr.dac.voos.listeners;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import br.ufpr.dac.voos.services.VooService;
import br.ufpr.dac.voos.models.ReservaTracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@Component
public class VooSagaListener {
    private static final Logger logger = LoggerFactory.getLogger(VooSagaListener.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private VooService vooService;

    @RabbitListener(queues = "voo.atualizacao", ackMode = "MANUAL")
    public void handleVooUpdate(Map<String, Object> payload, Message message, Channel channel) {
        try {
            logger.info("Received voo update request: {}", payload);

            Long reservaId = convertToLong(payload.get("reservaId"));
            Long vooId = convertToLong(payload.get("vooId"));
            Integer quantidade = (Integer) payload.get("quantidade");
            String status = (String) payload.get("status");

            ReservaTracking tracking = new ReservaTracking(
                reservaId,
                quantidade,
                status
            );

            boolean success = vooService.adicionarReservaTracking(vooId, tracking);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("reservaId", reservaId);
            
            if (success) {
                response.put("status", "success");
                response.put("message", "Voo successfully updated");
                logger.info("Successfully updated voo {} for reserva {}", vooId, reservaId);
            } else {
                response.put("status", "failure");
                response.put("message", "Failed to update voo");
                logger.error("Failed to update voo {} for reserva {}", vooId, reservaId);
            }

            // Send response back to orchestrator
            rabbitTemplate.convertAndSend("saga-exchange", "voo.atualizacao.response", response);
            
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            
        } catch (Exception e) {
            logger.error("Error processing voo update", e);
            sendErrorResponse(payload, e.getMessage());
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception ex) {
                logger.error("Error during message rejection", ex);
            }
        }
    }

    private void sendErrorResponse(Map<String, Object> originalPayload, String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("reservaId", originalPayload.get("reservaId"));
        response.put("status", "failure");
        response.put("message", errorMessage);
        
        rabbitTemplate.convertAndSend("saga-exchange", "voo.atualizacao.response", response);
    }

    private Long convertToLong(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        throw new IllegalArgumentException("Value must be either Integer or Long");
    }
}