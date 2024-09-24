package ca.fubi.dac.auth.dto;

import ca.fubi.dac.user.UserDTO;

public record AuthTokenDTO(String token, UserDTO user) {
}
