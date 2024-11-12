package br.ufpr.dac.MSClientes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.ufpr.dac.MSClientes.models.Usuario;
import br.ufpr.dac.MSClientes.repository.ClienteRepo;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepo clienteRepo;

    public boolean verificarCliente(String email, String senha) {
        return clienteRepo.findByEmail(email)
                .map(usuario -> usuario.verificarSenha(senha))
                .orElse(false);  
}
}
