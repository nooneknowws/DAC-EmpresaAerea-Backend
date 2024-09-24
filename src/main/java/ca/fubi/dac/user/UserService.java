package ca.fubi.dac.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.fubi.dac.auth.dto.CreateUserDTO;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public Optional<UserDTO> getUserByEmail(String email) {
	    return userRepository.findByEmail(email)
                 .map(user -> new UserDTO(
                     user.getId(),
                     user.getCpf(),
                     user.getNome(),
                     user.getEmail(),
                     user.getEndereco()));
	}

	public Optional<User> getUserByNome(String nome) {
		return userRepository.findByNome(nome);
	}

	public UserDTO createUser(CreateUserDTO createUserDto) {
		User user = new User();
		user.setNome(createUserDto.nome());
		user.setCpf(createUserDto.cpf());
		user.setEmail(createUserDto.email());
		user.setSenha(createUserDto.senha());
		
	    Endereco endereco = createUserDto.endereco();
	    endereco.setUser(user);
	    
	    Endereco savedEndereco = enderecoRepository.save(endereco);
	    user.setEndereco(savedEndereco);
	    
	    User savedUser = userRepository.save(user);
	    return new UserDTO(savedUser.getId(), savedUser.getCpf(), savedUser.getNome(), savedUser.getEmail(), savedEndereco);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}