package br.ufpr.dac.reserva.cqrs.query;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.ufpr.dac.reserva.dto.ReservaDTO;
import br.ufpr.dac.reserva.model.HistoricoAlteracaoEstado;

@Service
public class ReservaQueryService {

    @Autowired
    private ReservaQueryRepository reservaQueryRepository;
    
    public List<ReservaDTO> listarTodasReservas() {
        return reservaQueryRepository.findAll().stream()
            .map(reserva -> new ReservaDTO(
                reserva.getId(),
                reserva.getDataHora(),
                reserva.getAeroportoOrigemId(),
                reserva.getAeroportoDestinoId(),
                reserva.getValor(),
                reserva.getMilhas(),
                reserva.getStatus().name(),
                reserva.getVooId(),
                reserva.getClienteId(),
                reserva.getHistoricoAlteracaoEstado().stream()
                    .map(HistoricoAlteracaoEstado::toDTO)
                    .toList()
            ))
            .toList();
    }
    
    public Optional<ReservaDTO> consultarReserva(Long id) {
        return reservaQueryRepository.findById(id).map(reserva -> new ReservaDTO(
            reserva.getId(),
            reserva.getDataHora(),
            reserva.getAeroportoOrigemId(),
            reserva.getAeroportoDestinoId(),
            reserva.getValor(),
            reserva.getMilhas(),
            reserva.getStatus().name(),
            reserva.getVooId(),
            reserva.getClienteId(),
            reserva.getHistoricoAlteracaoEstado().stream()
                .map(HistoricoAlteracaoEstado::toDTO)
                .toList()
        ));
    }

    public List<ReservaDTO> listarReservasPorCliente(Long clienteId) {
        return reservaQueryRepository.findByClienteId(clienteId).stream()
            .map(reserva -> new ReservaDTO(
                reserva.getId(),
                reserva.getDataHora(),
                reserva.getAeroportoOrigemId(),
                reserva.getAeroportoDestinoId(),
                reserva.getValor(),
                reserva.getMilhas(),
                reserva.getStatus().name(),
                reserva.getVooId(),
                reserva.getClienteId(),
                reserva.getHistoricoAlteracaoEstado().stream()
                    .map(HistoricoAlteracaoEstado::toDTO)
                    .toList()
            ))
            .toList();
    }
    
    public List<ReservaDTO> listarReservasPorVoo(Long vooId) {
        return reservaQueryRepository.findByVooId(vooId).stream()
            .map(reserva -> new ReservaDTO(
                reserva.getId(),
                reserva.getDataHora(),
                reserva.getAeroportoOrigemId(),
                reserva.getAeroportoDestinoId(),
                reserva.getValor(),
                reserva.getMilhas(),
                reserva.getStatus().name(),
                reserva.getVooId(),
                reserva.getClienteId(),
                reserva.getHistoricoAlteracaoEstado().stream()
                    .map(HistoricoAlteracaoEstado::toDTO)
                    .toList()
            ))
            .toList();
    }
}