package com.funcionarios.funcionarios.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.funcionarios.funcionarios.models.Usuario;
import com.funcionarios.funcionarios.models.dto.LoginDTO;
import com.funcionarios.funcionarios.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

  @Autowired
    private FuncionarioRepository funcionarioRepository;

    public LoginDTO verificarFuncionario(String email, String senha) {
        return funcionarioRepository.findByEmail(email)
                .filter(usuario -> usuario.verificarSenha(senha))
                .map(usuario -> new LoginDTO(usuario.getEmail(), usuario.getSenha(), usuario.getId(), usuario.getPerfil()))
                .orElse(null); 
    }
}
