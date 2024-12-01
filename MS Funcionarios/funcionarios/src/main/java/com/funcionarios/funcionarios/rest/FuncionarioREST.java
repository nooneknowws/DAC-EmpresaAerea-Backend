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

import com.funcionarios.funcionarios.models.Usuario;
import com.funcionarios.funcionarios.models.dto.UsuarioDTO;
import com.funcionarios.funcionarios.repository.FuncionarioRepository;
import com.funcionarios.funcionarios.services.FuncionarioService;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin
@RequestMapping("/employees")
public class FuncionarioRest {
    private static final Logger logger = LoggerFactory.getLogger(FuncionarioRest.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;

    private final ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/register")
    public ResponseEntity<Object> inserirFuncionario(@RequestBody @Validated UsuarioDTO usuarioDTO) {
        try {
            if (funcionarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("E-mail já está em uso.");
            }

            Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
            usuario.setSalt(gerarSalt());
            usuario.setSenha(hashSenha(usuarioDTO.getSenha(), usuario.getSalt()));

            funcionarioRepository.save(usuario);

            UsuarioDTO savedFuncionarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
            savedFuncionarioDTO.setSenha(null); // Não retornar senha na resposta

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
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        List<Usuario> lista = funcionarioRepository.findAll();
        List<UsuarioDTO> usuariosDTO = lista.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneFuncionario(@PathVariable(value = "id") Long id) {
        Optional<Usuario> funcionario = funcionarioRepository.findById(id);
        if (funcionario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionário com ID " + id + " não encontrado.");
        }
        UsuarioDTO usuarioDTO = modelMapper.map(funcionario.get(), UsuarioDTO.class);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> updateFuncionario(@PathVariable(value = "id") Long id,
            @RequestBody @Validated UsuarioDTO usuarioDTO) {
        try {
            Optional<Usuario> funcionarioOptional = funcionarioRepository.findById(id);
            if (funcionarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Funcionário com ID " + id + " não encontrado.");
            }

            Usuario funcionario = funcionarioOptional.get();

            Optional<Usuario> usuarioComMesmoEmail = funcionarioRepository.findByEmail(usuarioDTO.getEmail());
            if (usuarioComMesmoEmail.isPresent() && !usuarioComMesmoEmail.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("O email informado já está em uso por outro funcionário.");
            }

            funcionario.setCpf(usuarioDTO.getCpf());
            funcionario.setNome(usuarioDTO.getNome());
            funcionario.setEmail(usuarioDTO.getEmail());
            funcionario.setPerfil(usuarioDTO.getPerfil());
            funcionario.setTelefone(usuarioDTO.getTelefone());

            if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
                funcionario.setSenha(hashSenha(usuarioDTO.getSenha(), funcionario.getSalt()));
            }

            funcionarioRepository.save(funcionario);

            UsuarioDTO updatedFuncionarioDTO = modelMapper.map(funcionario, UsuarioDTO.class);
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
        Optional<Usuario> funcionarioOptional = funcionarioRepository.findById(id);
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
