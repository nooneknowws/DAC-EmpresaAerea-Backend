package br.ufpr.dac.MSClientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.dac.MSClientes.models.Usuario;
import jakarta.transaction.Transactional;

public interface ClienteRepo extends JpaRepository<Usuario, Long> {

}
