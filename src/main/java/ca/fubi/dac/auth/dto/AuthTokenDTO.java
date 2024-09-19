package ca.fubi.dac.auth.dto;

import ca.fubi.user.UserDTO;

public record AuthTokenDTO(String token, UserDTO user) {
}
