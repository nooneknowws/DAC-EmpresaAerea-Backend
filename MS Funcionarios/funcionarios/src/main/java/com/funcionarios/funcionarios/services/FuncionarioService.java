package com.funcionarios.funcionarios.services;

import com.funcionarios.funcionarios.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.funcionarios.funcionarios.models.Funcionario;
import com.funcionarios.funcionarios.models.dto.LoginDTO;
import com.funcionarios.funcionarios.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public VerificationResult verificarFuncionario(String email, String senha) {
        return funcionarioRepository.findByEmail(email)
            .map(usuario -> {
                if (usuario.verificarSenha(senha)) {
                    return new VerificationResult(new LoginDTO(usuario.getEmail(), usuario.getSenha(), usuario.getId(), usuario.getPerfil(), usuario.getStatus()));
                } else {
                    return new VerificationResult("wrong_password");
                }
            })
            .orElse(new VerificationResult("email_not_found"));
    }
}
