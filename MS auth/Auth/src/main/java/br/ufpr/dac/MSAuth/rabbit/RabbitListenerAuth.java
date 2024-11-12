package br.ufpr.dac.MSAuth.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import br.ufpr.dac.MSAuth.service.JwtService;
import io.jsonwebtoken.io.IOException;

@Component
public class RabbitListenerAuth {

    private static final Logger logger = LoggerFactory.getLogger(RabbitListenerAuth.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "auth.response", ackMode = "MANUAL")
    public void handleAuthResponse(Map<String, String> response, Message message, Channel channel) {
        try {
            String status = response.get("status");
            Map<String, String> replyMessage = new HashMap<>();

            if ("success".equals(status)) {
                String email = response.get("email");
                String token = jwtService.generateToken(email);

                replyMessage.put("status", "success");
                replyMessage.put("token", token);
                replyMessage.put("email", email);
                logger.info("Login successful, JWT token generated for email: {}", email);
            } else {
                replyMessage.put("status", "error");
                replyMessage.put("message", response.get("message"));
                logger.warn("Login failed: {}", response.get("message"));
            }

            rabbitTemplate.convertAndSend("auth.reply", replyMessage);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("Error handling auth response", e);
            try {
                try {
					channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
				} catch (java.io.IOException e1) {
					e1.printStackTrace();
				}
            } catch (IOException ioException) {
                logger.error("Error during message rejection", ioException);
            }
        }
    }
}
