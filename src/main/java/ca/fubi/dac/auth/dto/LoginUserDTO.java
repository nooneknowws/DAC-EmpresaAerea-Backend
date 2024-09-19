package ca.fubi.dac.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserDTO(@NotBlank @Size(max = 64) String email, @NotBlank @Size(max = 128) String senha) {
}
