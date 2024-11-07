package br.ufpr.dac.MSAuth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Gera um token JWT para o usuário com base no e-mail
     * @param email O email do usuário
     * @return Token JWT gerado
     */
    public String generateToken(String email) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 300000)) // Token expira em 5 minutos
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        return builder.compact();
    }

    /**
     * Valida o token JWT recebido
     * @param token Token JWT
     * @return true se o token for válido; false caso contrário
     */
    public boolean validateToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build();
            parser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrai o email do usuário a partir do token JWT
     * @param token Token JWT
     * @return O email extraído do token
     */
    public String getEmailFromToken(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build();

        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
