package br.ufpr.dac.MSReserva.cqrs.query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufpr.dac.MSReserva.model.Reserva;

@Repository
public interface ReservaQueryRepository extends JpaRepository<Reserva, Long> {

	 @Query("SELECT DISTINCT r FROM Reserva r LEFT JOIN FETCH r.historicoAlteracaoEstado WHERE r.clienteId = :clienteId")
	    List<Reserva> findByClienteId(@Param("clienteId") Long clienteId);

	    @Query("SELECT DISTINCT r FROM Reserva r LEFT JOIN FETCH r.historicoAlteracaoEstado WHERE r.id = :id")
	    Reserva findReservaById(@Param("id") Long id);

	    @Query("SELECT DISTINCT r FROM Reserva r LEFT JOIN FETCH r.historicoAlteracaoEstado WHERE r.vooId = :vooId")
	    List<Reserva> findByVooId(@Param("vooId") Long vooId);
}
