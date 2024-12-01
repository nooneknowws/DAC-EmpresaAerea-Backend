package br.ufpr.dac.MSClientes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.ufpr.dac.MSClientes.models.Usuario;
import br.ufpr.dac.MSClientes.models.dto.LoginDTO;
import br.ufpr.dac.MSClientes.repository.ClienteRepo;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepo clienteRepo;

    public LoginDTO verificarCliente(String email, String senha) {
        return clienteRepo.findByEmail(email)
                .filter(usuario -> usuario.verificarSenha(senha))
                .map(usuario -> new LoginDTO(usuario.getEmail(), usuario.getSenha(), usuario.getId(), usuario.getPerfil()))
                .orElse(null); 
    }
}
