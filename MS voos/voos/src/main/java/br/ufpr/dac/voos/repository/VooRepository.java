package br.ufpr.dac.voos.repository;

import br.ufpr.dac.voos.models.Voo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface VooRepository extends JpaRepository<Voo, Long> {
    @Query("SELECT v FROM Voo v JOIN v.origem JOIN v.destino")
    List<Voo> findAllWithOrigemAndDestino();

    @Query("SELECT v FROM Voo v JOIN v.origem JOIN v.destino WHERE v.id = :id")
    Optional<Voo> findByIdWithOrigemAndDestino(@Param("id") Long id);
}