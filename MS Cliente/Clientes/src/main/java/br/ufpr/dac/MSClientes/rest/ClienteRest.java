package br.ufpr.dac.MSClientes.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException; 
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.ufpr.dac.MSClientes.models.Usuario;
import br.ufpr.dac.MSClientes.models.dto.UsuarioDTO;
import br.ufpr.dac.MSClientes.repository.ClienteRepo;
import jakarta.transaction.Transactional;

@RestController
@CrossOrigin
public class ClienteRest {
	private static final Logger logger = LoggerFactory.getLogger(ClienteRest.class);
    @Autowired
    private ClienteRepo clienteRepository;


    private final ModelMapper modelMapper = new ModelMapper();
    
   
    @Transactional(dontRollbackOn = {DataIntegrityViolationException.class})
    @PostMapping("/cadastro/clientes")
    public ResponseEntity<UsuarioDTO> inserirFuncionario(@RequestBody @Validated UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
            usuario.setSalt(gerarSalt());
            usuario.setSenha(hashSenha(usuarioDTO.getSenha(), usuario.getSalt())); 

            clienteRepository.save(usuario);

            UsuarioDTO savedFuncionarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
            savedFuncionarioDTO.setSenha(null);

            return new ResponseEntity<>(savedFuncionarioDTO, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados: {}", e.getMessage());

            String errorMessage = e.getRootCause().getMessage();
            if (errorMessage.contains("cpf")) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else if (errorMessage.contains("email")) {
                return new ResponseEntity<>(HttpStatus.CONFLICT); 
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    @GetMapping("/usuarios/clientes")
    List<UsuarioDTO> listarTodos(){
    	List<Usuario> lista = clienteRepository.findAll();
    	return lista.stream().map(e -> modelMapper.map(e, UsuarioDTO.class)).collect(Collectors.toList());
    }

    private String gerarSalt() {
    	   byte[] salt = new byte[16];
           SecureRandom random = new SecureRandom();
           random.nextBytes(salt);
           return Base64.getEncoder().encodeToString(salt);
    }

    private String hashSenha(String senha, String salt) {
    	try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt)); 
            byte[] hashedPassword = md.digest(senha.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar a senha", e);
        }
    }

}