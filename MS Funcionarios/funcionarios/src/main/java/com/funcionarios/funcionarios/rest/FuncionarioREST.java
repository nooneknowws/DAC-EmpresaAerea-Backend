package com.funcionarios.funcionarios.rest;

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

import com.funcionarios.funcionarios.models.Usuario;
import com.funcionarios.funcionarios.models.dto.UsuarioDTO;
import com.funcionarios.funcionarios.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;

@RestController
@CrossOrigin
public class FuncionarioRest {
	private static final Logger logger = LoggerFactory.getLogger(FuncionarioRest.class);
    @Autowired
    private FuncionarioRepository funcionarioRepository;


    private final ModelMapper modelMapper = new ModelMapper();
    
   
    @Transactional(dontRollbackOn = {DataIntegrityViolationException.class})
    @PostMapping("/cadastro/funcionarios")
    public ResponseEntity<UsuarioDTO> inserirFuncionario(@RequestBody @Validated UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
            usuario.setSalt(gerarSalt());
            usuario.setSenha(hashSenha(usuarioDTO.getSenha(), usuario.getSalt())); 

            funcionarioRepository.save(usuario);

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
    
    @GetMapping("/usuarios/funcionarios")
    List<UsuarioDTO> listarTodos(){
    	List<Usuario> lista = funcionarioRepository.findAll();
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
