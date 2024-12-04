package br.ufpr.dac.reserva.cqrs.command;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.ufpr.dac.reserva.model.Reserva;
import br.ufpr.dac.reserva.model.StatusReserva;

@Repository
@Primary
public interface ReservaCommandRepository extends JpaRepository<Reserva, Long> {

    @Modifying
    @Query("UPDATE Reserva r SET r.status = :novoStatus WHERE r.id = :id")
    void atualizarStatusReserva(@Param("id") Long id, @Param("novoStatus") StatusReserva novoStatus);

    @Modifying
    @Query("DELETE FROM Reserva r WHERE r.id = :id")
    void excluirReserva(@Param("id") Long id);
}