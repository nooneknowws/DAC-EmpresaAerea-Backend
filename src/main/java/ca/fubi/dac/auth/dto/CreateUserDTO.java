package ca.fubi.dac.auth.dto;

import ca.fubi.dac.user.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
		Long cpf,
		@NotBlank @Size(max = 64) String nome,  
		@NotBlank @Size(max = 64) String email,
		@NotBlank @Size(max = 128) String senha,
		Endereco endereco) {
}