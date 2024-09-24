package ca.fubi.dac.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
		Long id, 
		Long cpf, 
		@NotBlank @Size(max = 64) String nome, 
		@NotBlank @Size(max = 64) String email,
		Endereco endereco) {
	public UserDTO(User user) {
		this(user.getId(), user.getCpf(), user.getNome(), user.getEmail(), user.getEndereco());
	}
}