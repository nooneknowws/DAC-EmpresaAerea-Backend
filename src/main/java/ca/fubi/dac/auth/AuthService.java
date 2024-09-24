package ca.fubi.dac.auth;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import ca.fubi.dac.auth.dto.AuthTokenDTO;
import ca.fubi.dac.auth.dto.LoginUserDTO;
import ca.fubi.dac.user.UserDTO;
import ca.fubi.dac.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

	@Autowired
	private UserService userService;

	private String secret = "cf7fd04b-9ff3-4dd2-84fe-a146051d41fe";

	public AuthTokenDTO authenticateUser(LoginUserDTO loginUserDTO) {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		String token = Jwts.builder().setSubject(loginUserDTO.email()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dia
				.signWith(key).compact();
		return new AuthTokenDTO(token,
				userService.getUserByEmail(loginUserDTO.email()).map(UserDTO::new).orElseThrow());
	}

	public AuthTokenDTO generateToken(UserDTO userDTO) {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		String token = Jwts.builder().setSubject(userDTO.email()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000)).signWith(key).compact();

		return new AuthTokenDTO(token, userDTO);
	}

	public ResponseCookie clearToken() {
		return ResponseCookie.from("auth-user", "").maxAge(0).httpOnly(true).path("/").build();
	}
}