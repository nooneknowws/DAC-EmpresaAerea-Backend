package ca.fubi.dac.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.fubi.dac.auth.dto.AuthTokenDTO;
import ca.fubi.dac.auth.dto.LoginUserDTO;
import ca.fubi.user.UserDTO;
import ca.fubi.user.UserService;
import ca.fubi.dac.auth.dto.CreateUserDTO;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = "*", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<AuthTokenDTO> authenticateUser(@RequestBody LoginUserDTO loginUserDto) {
		AuthTokenDTO token = authService.authenticateUser(loginUserDto);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO createUserDto) {
		UserDTO userDTO = userService.createUser(createUserDto);
		return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
	}

	@PostMapping("/logout")
	public ResponseEntity<AuthTokenDTO> clearToken() {
		ResponseCookie cookie = authService.clearToken();
		AuthTokenDTO token = new AuthTokenDTO(cookie.toString(), null);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(token);
	}
}