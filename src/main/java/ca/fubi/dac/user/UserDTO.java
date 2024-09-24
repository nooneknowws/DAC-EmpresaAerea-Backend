package ca.fubi.dac.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(Long id, @NotBlank @Size(max = 64) String nome, @NotBlank @Size(max = 64) String email) {
	public UserDTO(User user) {
		this(user.getId(), user.getNome(), user.getEmail());
	}
}