package com.funcionarios.funcionarios.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.funcionarios.funcionarios.models.Funcionario;
import com.funcionarios.funcionarios.models.dto.FuncionarioDTO;
import com.funcionarios.funcionarios.models.dto.FuncionarioDTO;
import com.funcionarios.funcionarios.repository.FuncionarioRepository;
import com.funcionarios.funcionarios.services.FuncionarioService;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin
@RequestMapping("/funcionarios")
public class FuncionarioRest {
    private static final Logger logger = LoggerFactory.getLogger(FuncionarioRest.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;

    private final ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/cadastro")
    public ResponseEntity<Object> inserirFuncionario(@RequestBody @Validated FuncionarioDTO funcionarioDTO) {
        try {
            if (funcionarioRepository.findByEmail(funcionarioDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("E-mail já está em uso.");
            }

            Funcionario usuario = modelMapper.map(funcionarioDTO, Funcionario.class);
            usuario.setSalt(gerarSalt());
            usuario.setSenha(hashSenha(funcionarioDTO.getSenha(), usuario.getSalt()));

            funcionarioRepository.save(usuario);

            FuncionarioDTO savedFuncionarioDTO = modelMapper.map(usuario, FuncionarioDTO.class);
            savedFuncionarioDTO.setSenha(null); 

            return ResponseEntity.status(HttpStatus.CREATED).body(savedFuncionarioDTO);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao cadastrar o funcionário: CPF ou Email duplicado.");
        } catch (Exception e) {
            logger.error("Erro inesperado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno no servidor. Tente novamente mais tarde.");
        }
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> listarTodos() {
        List<Funcionario> lista = funcionarioRepository.findAll();
        List<FuncionarioDTO> usuariosDTO = lista.stream()
                .map(usuario -> modelMapper.map(usuario, FuncionarioDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneFuncionario(@PathVariable(value = "id") Long id) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        if (funcionario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionário com ID " + id + " não encontrado.");
        }
        FuncionarioDTO funcionarioDTO = modelMapper.map(funcionario.get(), FuncionarioDTO.class);
        return ResponseEntity.ok(funcionarioDTO);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> updateFuncionario(@PathVariable(value = "id") Long id,
            @RequestBody @Validated FuncionarioDTO FuncionarioDTO) {
        try {
            Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(id);
            if (funcionarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Funcionário com ID " + id + " não encontrado.");
            }

            Funcionario funcionario = funcionarioOptional.get();

            Optional<Funcionario> usuarioComMesmoEmail = funcionarioRepository.findByEmail(FuncionarioDTO.getEmail());
            if (usuarioComMesmoEmail.isPresent() && !usuarioComMesmoEmail.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("O email informado já está em uso por outro funcionário.");
            }

            funcionario.setCpf(FuncionarioDTO.getCpf());
            funcionario.setNome(FuncionarioDTO.getNome());
            funcionario.setEmail(FuncionarioDTO.getEmail());
            funcionario.setPerfil(FuncionarioDTO.getPerfil());
            funcionario.setTelefone(FuncionarioDTO.getTelefone());

            if (FuncionarioDTO.getSenha() != null && !FuncionarioDTO.getSenha().isEmpty()) {
                funcionario.setSenha(hashSenha(FuncionarioDTO.getSenha(), funcionario.getSalt()));
            }

            funcionarioRepository.save(funcionario);

            FuncionarioDTO updatedFuncionarioDTO = modelMapper.map(funcionario, FuncionarioDTO.class);
            updatedFuncionarioDTO.setSenha(null);

            return ResponseEntity.ok(updatedFuncionarioDTO);
        } catch (Exception e) {
            logger.error("Erro ao atualizar funcionário com ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao atualizar o funcionário. Detalhes: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteFuncionario(@PathVariable(value = "id") Long id) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(id);
        if (funcionarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionário com ID " + id + " não encontrado.");
        }
        funcionarioRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
