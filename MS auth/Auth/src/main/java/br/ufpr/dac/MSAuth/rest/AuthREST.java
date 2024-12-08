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
import br.ufpr.dac.MSAuth.model.AuthSession;
import br.ufpr.dac.MSAuth.repository.AuthSessionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class AuthREST {
	private static final Logger logger = LoggerFactory.getLogger(AuthREST.class);
	private static final int RABBIT_TIMEOUT_SECONDS = 10;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthSessionRepository authSessionRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        if (!isValidLoginRequest(loginDTO)) {
            logger.warn("Invalid login request received");
            return createErrorResponse("Invalid login credentials", 400);
        }

        logger.info("Processing login request for email: {}", loginDTO.getEmail());

        Optional<AuthSession> existingSession = authSessionRepository.findByEmail(loginDTO.getEmail());
        if (existingSession.isPresent()) {
            AuthSession session = existingSession.get();
            if (jwtService.validateToken(session.getToken())) {
                logger.info("Duplicate login detected for email: {}. Deleting old session and continuing with new login.", loginDTO.getEmail());
                authSessionRepository.delete(session);
            } else {
                logger.info("Cleaning up expired session for email: {}", loginDTO.getEmail());
                authSessionRepository.delete(session);
            }
        }

        Map<String, String> authResponse = authenticateViaRabbitMQ(loginDTO);
        if (authResponse == null) {
            return createErrorResponse("Authentication service unavailable", 504);
        }

        if ("success".equals(authResponse.get("status"))) {
            return handleSuccessfulAuth(authResponse);
        } else {
            return createErrorResponse(
                authResponse.getOrDefault("message", "Authentication failed"), 
                401
            );
        }
    }


    private boolean isValidLoginRequest(LoginDTO loginDTO) {
        return loginDTO != null 
            && loginDTO.getEmail() != null 
            && !loginDTO.getEmail().trim().isEmpty()
            && loginDTO.getSenha() != null 
            && !loginDTO.getSenha().trim().isEmpty();
    }

    private Map<String, String> authenticateViaRabbitMQ(LoginDTO loginDTO) {
        try {
            rabbitTemplate.convertAndSend("saga-exchange", "auth.request", loginDTO);
            logger.info("Sent authentication request to RabbitMQ");

            Message responseMessage = rabbitTemplate.receive(
                "auth.response", 
                TimeUnit.SECONDS.toMillis(RABBIT_TIMEOUT_SECONDS)
            );

            if (responseMessage == null) {
                logger.error("No response received from authentication service");
                return null;
            }

            @SuppressWarnings("unchecked")
            Map<String, String> response = (Map<String, String>) rabbitTemplate
                .getMessageConverter()
                .fromMessage(responseMessage);

            logger.info("Received authentication response for email: {}", loginDTO.getEmail());
            return response;

        } catch (Exception e) {
            logger.error("Error during RabbitMQ communication: {}", e.getMessage());
            return null;
        }
    }

    private ResponseEntity<?> handleSuccessfulAuth(Map<String, String> authResponse) {
        try {
            String id = authResponse.get("id");
            String email = authResponse.get("email");
            String perfil = authResponse.get("perfil");
            String statusFunc = authResponse.get("statusFunc");
            
            String token = jwtService.generateToken(email);

            AuthSession newSession = new AuthSession(id, email, token, perfil, statusFunc);
            authSessionRepository.save(newSession);
            logger.info("Created new session for user: {}", email);

            Map<String, String> response = new HashMap<>();
            response.put("id", id);
            response.put("status", "success");
            response.put("token", token);
            response.put("email", email);
            response.put("perfil", perfil);
            response.put("statusFunc", statusFunc);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error creating user session: {}", e.getMessage());
            return createErrorResponse("Error creating user session", 500);
        }
    }

    private ResponseEntity<?> createErrorResponse(String message, int statusCode) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);
        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        
        if (token == null || token.trim().isEmpty()) {
            logger.warn("Logout attempt with missing token");
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Token is required"));
        }

        try {
            if (jwtService.validateToken(token)) {
                String userEmail = jwtService.getEmailFromToken(token);
                logger.info("Processing logout for user: {}", userEmail);
                
                boolean sessionRemoved = false;
                try {
                    var session = authSessionRepository.findByToken(token);
                    if (session.isPresent()) {
                        authSessionRepository.delete(session.get());
                        sessionRemoved = true;
                        logger.info("Successfully deleted session for user: {}", userEmail);
                    } else {
                        var emailSession = authSessionRepository.findByEmail(userEmail);
                        if (emailSession.isPresent()) {
                            authSessionRepository.delete(emailSession.get());
                            sessionRemoved = true;
                            logger.info("Successfully deleted session using email for user: {}", userEmail);
                        }
                    }
                } catch (Exception dbError) {
                    logger.error("Database error during session cleanup: {}", dbError.getMessage());
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Logout successful");
                response.put("sessionCleanedUp", sessionRemoved);
                
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Logout attempted with invalid token");
                return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid token"));
            }
        } catch (Exception e) {
            logger.error("Error during logout", e);
            return ResponseEntity.status(500)
                .body(Map.of("message", "Error processing logout"));
        }
    }
    @GetMapping("/session/check")
    public ResponseEntity<?> checkSession(@RequestHeader("x-access-token") String token) {
        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", "No token provided"));
        }

        try {
            if (jwtService.validateToken(token)) {
                String email = jwtService.getEmailFromToken(token);
                Optional<AuthSession> session = authSessionRepository.findByEmail(email);
                
                if (session.isPresent() && session.get().getToken().equals(token)) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("token", token);
                    response.put("user", session.get());
                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", "Invalid session"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", "error", "message", "Error checking session"));
        }
    }
}