package br.ufpr.dac.MSAuth.rest;
import java.util.*;

import org.springframework.web.bind.annotation.*;

import br.ufpr.dac.MSAuth.model.Usuario;

@CrossOrigin
@RestController
public class UsuarioREST {
	public static List<Usuario> lista = new ArrayList<>();
	
	@GetMapping("/usuarios")
	public List<Usuario> obterTodosUsuarios() {
		return lista;
	}
	
	static {
		lista.add(new Usuario(1, "adm", "admin", "admin", "ADMIN"));
		lista.add(new Usuario(2, "gerent", "gerente", "123", "GERENTE"));
		lista.add(new Usuario(3, "func", "funcionario", "123", "FUNC"));
	}

}
