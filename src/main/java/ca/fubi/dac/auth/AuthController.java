package ca.fubi.dac.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.fubi.dac.auth.dto.AuthTokenDTO;
import ca.fubi.dac.auth.dto.LoginUserDTO;
import ca.fubi.dac.user.UserService;
import ca.fubi.dac.auth.dto.CreateUserDTO;

@CrossOrigin(origins = "http://localhost:30053")
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
	public ResponseEntity<AuthTokenDTO> createUser(@RequestBody CreateUserDTO createUserDto) {
		AuthTokenDTO token = authService.generateToken(userService.createUser(createUserDto));
		return new ResponseEntity<>(token, HttpStatus.CREATED);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> clearToken() {
		authService.clearToken();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}