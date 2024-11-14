package br.ufpr.dac.MSAuth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Base64;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secretKey;

    private Key signingKey;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey); 
        if (keyBytes.length < 32) {  
            throw new WeakKeyException("A chave especificada é muito fraca para o algoritmo de assinatura HS256. Deve ter pelo menos 256 bits.");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .claim("sub", email) 
                .claim("iat", new Date().getTime()) 
                .setExpiration(new Date(System.currentTimeMillis() + 300000)) 
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(signingKey) 
                    .build();
            parser.parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("sub", String.class);
    }
}
