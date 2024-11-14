package com.funcionarios.funcionarios.rest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.funcionarios.funcionarios.repository.FuncionarioRepository;

public class FuncionarioREST {
  private static final Logger logger = LoggerFactory.getLogger(FuncionarioREST.class);
    @Autowired
    private FuncionarioRepository funcionarioRepository;


    private final ModelMapper modelMapper = new ModelMapper();
    
   
    @Transactional(dontRollbackOn = {DataIntegrityViolationException.class})
    @PostMapping("/cadastro/funcionario")
    public ResponseEntity<FuncionarioDTO> inserirFuncionario(@RequestBody @Validated FuncionarioDTO funcionarioDTo) {
        try {
            Funcionario funcionario = modelMapper.map(FuncionarioDTO, Funcionario.class);
            funcionario.setSalt(gerarSalt());
            funcionario.setSenha(hashSenha(FuncionarioDTO.getSenha(), funcionario.getSalt())); 

            FuncionarioRepository.save(usuario);

            FuncionarioDTO savedFuncionarioDTO = modelMapper.map(funcionario, FuncionarioDTO.class);
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
    List<FuncionarioDTO> listarTodos(){
    	List<Funcionario> lista = FuncionarioRepository.findAll();
    	return lista.stream().map(e -> modelMapper.map(f, FuncionarioDTO.class)).collect(Collectors.toList());
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
