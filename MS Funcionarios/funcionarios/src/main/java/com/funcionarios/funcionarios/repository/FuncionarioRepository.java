package com.funcionarios.funcionarios.repository;

import java.util.Optional;
import com.funcionarios.funcionarios.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Usuario, Long> {

  Optional<Usuario> findByEmail(String email);
}
