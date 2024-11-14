package br.ufpr.dac.reserva;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufpr.dac.reserva.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
	Optional<Reserva> findById(Long id);
    List<Reserva> findByClienteId(Long clienteId);
    List<Reserva> findByVooId(Long vooId);
}
