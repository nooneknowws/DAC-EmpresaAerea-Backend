package br.ufpr.dac.MSAuth.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpr.dac.MSAuth.model.Login;
import br.ufpr.dac.MSAuth.model.Usuario;

@CrossOrigin
@RestController
public class AuthREST {
	
	@PostMapping("/login")
	ResponseEntity<Usuario> login(@RequestBody Login login) {
		if (login.getLogin().equals(login.getSenha())) {
			Usuario usu = new Usuario(1, login.getLogin(),login.getLogin(), login.getSenha(), "ADMIN");
			return ResponseEntity.ok().header("Content-Type", "application/json").body(usu);
		}
		else {
			return ResponseEntity.status(401).build();
		}
	}

}
