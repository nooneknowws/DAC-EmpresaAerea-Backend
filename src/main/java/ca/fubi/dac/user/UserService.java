package ca.fubi.dac.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.fubi.dac.auth.dto.CreateUserDTO;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> getUserByNome(String nome) {
		return userRepository.findByNome(nome);
	}

	public UserDTO createUser(CreateUserDTO createUserDto) {
		User user = new User();
		user.setNome(createUserDto.nome());
		user.setEmail(createUserDto.email());
		user.setSenha(createUserDto.senha());

		User savedUser = userRepository.save(user);
		return new UserDTO(savedUser.getId(), savedUser.getNome(), savedUser.getEmail());
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}