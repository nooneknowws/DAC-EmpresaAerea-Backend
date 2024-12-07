package br.ufpr.dac.MSAuth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    
    // Token expiration time in hours
    private static final long EXPIRATION_HOURS = 24;
    
    // The secret key used for signing tokens
    private final SecretKey secretKey;

    public JwtService(@Value("${JWT_SECRET}") String jwtSecret) {
        // In JJWT 0.12.6, we use Keys.hmacShaKeyFor to create a secure key
        // This ensures the key is properly sized for the HMAC-SHA algorithm
        this.secretKey = Keys.hmacShaKeyFor(
            jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
        logger.info("JWT Service initialized with secret key");
    }

    /**
     * Generates a new JWT token for a given email.
     * The token includes the email as the subject and standard timing claims.
     *
     * @param email The email to be included in the token
     * @return The generated JWT token string
     */
    public String generateToken(String email) {
        // Get the current timestamp and calculate expiration
        Instant now = Instant.now();
        Instant expiration = now.plus(EXPIRATION_HOURS, ChronoUnit.HOURS);

        // Build the token using the new JJWT fluent API
        return Jwts.builder()
                .subject(email)                // Set the subject (email)
                .issuedAt(Date.from(now))      // Set the issue timestamp
                .expiration(Date.from(expiration))  // Set the expiration
                .signWith(secretKey)           // Sign with our secret key
                .compact();                    // Build the final token
    }

    /**
     * Validates a JWT token for authenticity and expiration.
     * This method uses JJWT 0.12.6's new parser builder pattern.
     *
     * @param token The token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            logger.error("Received null or empty token");
            return false;
        }

        try {
            // Parse and validate the token using JJWT 0.12.6's new parser
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            
            // If we reach this point, the token is valid
            return true;

        } catch (SignatureException e) {
            // The token's signature doesn't match our key
            logger.error("Invalid JWT signature: {}", e.getMessage());
            return false;
            
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // The token has expired
            logger.error("JWT token has expired: {}", e.getMessage());
            return false;
            
        } catch (io.jsonwebtoken.security.SecurityException e) {
            // There was a security problem with the token
            logger.error("JWT security error: {}", e.getMessage());
            return false;
            
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // The token's structure is invalid
            logger.error("Malformed JWT token: {}", e.getMessage());
            return false;
            
        } catch (JwtException e) {
            // Catch any other JWT-specific exceptions
            logger.error("JWT validation error: {}", e.getMessage());
            return false;
            
        } catch (Exception e) {
            // Catch any unexpected errors
            logger.error("Unexpected error during JWT validation: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts the email (subject) from a JWT token.
     * Uses JJWT 0.12.6's new claims parsing approach.
     *
     * @param token The JWT token
     * @return The email from the token, or null if the token is invalid
     */
    public String getEmailFromToken(String token) {
        try {
            // Parse the token and extract claims using the new API
            var jwtData = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            
            // Get the claims body and extract the subject (email)
            Claims claims = jwtData.getPayload();
            return claims.getSubject();
            
        } catch (Exception e) {
            logger.error("Error extracting email from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Returns the token expiration time in milliseconds.
     * This can be useful for client-side token management.
     *
     * @return The expiration time in milliseconds
     */
    public long getExpirationTimeInMillis() {
        return EXPIRATION_HOURS * 60 * 60 * 1000;
    }
}