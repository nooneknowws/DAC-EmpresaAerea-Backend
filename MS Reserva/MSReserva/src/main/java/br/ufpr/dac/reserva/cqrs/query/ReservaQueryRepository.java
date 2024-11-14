package br.ufpr.dac.reserva.cqrs.query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.ufpr.dac.reserva.model.Reserva;

@Repository
public interface ReservaQueryRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.id = :id")
    Reserva findReservaById(@Param("id") Long id);

    @Query("SELECT r FROM Reserva r WHERE r.clienteId = :clienteId")
    List<Reserva> findByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT r FROM Reserva r WHERE r.vooId = :vooId")
    List<Reserva> findByVooId(@Param("vooId") Long vooId);
}
