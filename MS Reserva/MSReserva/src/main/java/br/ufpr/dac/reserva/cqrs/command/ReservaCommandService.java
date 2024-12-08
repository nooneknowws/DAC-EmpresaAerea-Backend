package br.ufpr.dac.reserva.cqrs.command;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.ufpr.dac.reserva.dto.CriarReservaDTO;
import br.ufpr.dac.reserva.model.Reserva;
import br.ufpr.dac.reserva.model.StatusReserva;
import jakarta.transaction.Transactional;

@Service
public class ReservaCommandService {

    @Autowired
    private ReservaCommandRepository reservaCommandRepository;

    @Transactional
    public Reserva criarReserva(CriarReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setDataHora(LocalDateTime.now());
        reserva.setAeroportoOrigemId(dto.aeroportoOrigemId());
        reserva.setAeroportoDestinoId(dto.aeroportoDestinoId());
        reserva.setValor(dto.valor());
        reserva.setMilhas(dto.milhas());
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setVooId(dto.vooId());
        reserva.setClienteId(dto.clienteId());
        reserva.setCodigoReserva(gerarCodigoReserva());

        reservaCommandRepository.save(reserva);
        return reserva;
    }

    @Transactional
    public Reserva atualizarStatusReserva(Long reservaId, StatusReserva novoStatus) {
        Reserva reserva = reservaCommandRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        reserva.adicionarHistoricoAlteracaoEstado(reserva.getStatus(), novoStatus);
        reserva.setStatus(novoStatus);
        reservaCommandRepository.save(reserva);
        return reserva;
    }

    private String gerarCodigoReserva() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 3).toUpperCase() + "-" + uuid.toString().substring(3, 6);
    }
}
