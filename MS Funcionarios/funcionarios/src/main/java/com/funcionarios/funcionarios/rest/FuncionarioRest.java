package com.funcionarios.funcionarios.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.funcionarios.funcionarios.models.Funcionario;
import com.funcionarios.funcionarios.models.dto.FuncionarioDTO;
import com.funcionarios.funcionarios.repository.FuncionarioRepository;
import com.funcionarios.funcionarios.services.FuncionarioService;

@RestController
@CrossOrigin
@RequestMapping("/funcionarios")
public class FuncionarioRest {
    private static final Logger logger = LoggerFactory.getLogger(FuncionarioRest.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ModelMapper modelMapper = new ModelMapper();
    
    @RabbitListener(queues = "employee.email.check")
    public void checkExistence(Map<String, String> request) {
        String email = request.get("email");
        String cpf = request.get("cpf");
        
        boolean emailExists = funcionarioRepository.findByEmail(email).isPresent();
        boolean cpfExists = funcionarioRepository.findByCpf(cpf).isPresent();
        
        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        response.put("emailExists", String.valueOf(emailExists));
        response.put("cpfExists", String.valueOf(cpfExists));
        
        rabbitTemplate.convertAndSend("saga-exchange", "employee.email.check.response", response);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Object> inserirFuncionario(@RequestBody @Validated FuncionarioDTO funcionarioDTO) {
        try {
            Map<String, Boolean> availability = funcionarioService
                .verifyRegistrationAvailability(funcionarioDTO.getEmail(), funcionarioDTO.getCpf())
                .get(30, TimeUnit.SECONDS);
            
            if (!availability.get("emailAvailable")) {
                logger.warn("Email already exists: {}", funcionarioDTO.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("E-mail já está em uso.");
            }
            
            if (!availability.get("cpfAvailable")) {
                logger.warn("CPF already exists: {}", funcionarioDTO.getCpf());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("CPF já está cadastrado.");
            }

            Funcionario usuario = modelMapper.map(funcionarioDTO, Funcionario.class);
            usuario.setSalt(gerarSalt());
            usuario.setSenha(hashSenha(funcionarioDTO.getSenha(), usuario.getSalt()));

            funcionarioRepository.save(usuario);

            FuncionarioDTO savedFuncionarioDTO = modelMapper.map(usuario, FuncionarioDTO.class);
            savedFuncionarioDTO.setSenha(null);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedFuncionarioDTO);
        } catch (TimeoutException e) {
            logger.error("Verification timeout for email: {}", funcionarioDTO.getEmail());
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body("Timeout na verificação.");
        } catch (Exception e) {
            logger.error("Error in registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno no servidor.");
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
    public ResponseEntity<Object> getOneFuncionario(@PathVariable(value = "id") Long id){
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
    @PutMapping("/status/{id}")
    public ResponseEntity<Object> updateFuncionarioStatus(@PathVariable(value = "id") Long id) {
        try {
            Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(id);
            if (funcionarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Funcionário com ID " + id + " não encontrado.");
            }
            Funcionario funcionario = funcionarioOptional.get();
            
            String currentStatus = funcionario.getfuncStatus();
            String newStatus = "ATIVO".equals(currentStatus) ? "INATIVO" : "ATIVO";
            
            funcionario.setfuncStatus(newStatus);

            funcionarioRepository.save(funcionario);

            FuncionarioDTO updatedFuncionarioDTO = modelMapper.map(funcionario, FuncionarioDTO.class);
          
            logger.info("Status do funcionário {} alterado de {} para {}", 
                id, currentStatus, newStatus);

            return ResponseEntity.ok(updatedFuncionarioDTO);
            
        } catch (Exception e) {
            logger.error("Erro ao atualizar status do funcionário com ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao atualizar o status do funcionário. Detalhes: " + e.getMessage());
        }
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
