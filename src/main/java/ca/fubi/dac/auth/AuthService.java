package ca.fubi.dac.auth;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import ca.fubi.dac.auth.dto.AuthTokenDTO;
import ca.fubi.dac.auth.dto.LoginUserDTO;
import ca.fubi.user.UserDTO;
import ca.fubi.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public AuthTokenDTO authenticateUser(LoginUserDTO loginUserDTO) {
        Key key = Keys.hmacShaKeyFor(new String("razer.net.br").getBytes());
        String token = Jwts.builder().setSubject(loginUserDTO.email()).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dia
                .signWith(key).compact();
        return new AuthTokenDTO(token,
                userService.getUserByEmail(loginUserDTO.email()).map(UserDTO::new).orElseThrow());
    }

    public ResponseCookie clearToken() {
        return ResponseCookie.from("auth-user", "").maxAge(0).httpOnly(true).path("/").build();
    }
}