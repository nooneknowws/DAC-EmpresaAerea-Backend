package br.ufpr.dac.MSReserva.cqrs.query;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufpr.dac.MSReserva.dto.ReservaDTO;
import br.ufpr.dac.MSReserva.model.HistoricoAlteracaoEstado;
import br.ufpr.dac.MSReserva.model.Reserva;

@Service
public class ReservaQueryService {

    @Autowired
    private ReservaQueryRepository reservaQueryRepository;
    
    private static final Function<Reserva, ReservaDTO> toDTO = reserva -> new ReservaDTO(
            reserva.getId(),
            reserva.getDataHora(),
            reserva.getAeroportoOrigemCod(),
            reserva.getAeroportoDestinoCod(),
            reserva.getValor(),
            reserva.getMilhas(),
            reserva.getStatus().name(),
            reserva.getVooId(),
            reserva.getClienteId(),
            reserva.getCodigoReserva(),
            reserva.getCodigoVoo(),
            reserva.getQuantidade(),
            reserva.getHistoricoAlteracaoEstado().stream()
                    .map(HistoricoAlteracaoEstado::toDTO)
                    .toList()
    );
    private List<ReservaDTO> convertToDTOList(List<Reserva> reservas) {
        return reservas.stream()
                .map(toDTO)
                .toList();
    }

    public List<ReservaDTO> listarTodasReservas() {
        return convertToDTOList(reservaQueryRepository.findAll());
    }

    public Optional<ReservaDTO> consultarReserva(Long id) {
        return reservaQueryRepository.findById(id).map(toDTO);
    }

    public List<ReservaDTO> listarReservasPorCliente(Long clienteId) {
        List<Reserva> reservas = reservaQueryRepository.findByClienteId(clienteId);
        List<ReservaDTO> dtos = convertToDTOList(reservas);
        
        return dtos;
    }

    public List<ReservaDTO> listarReservasPorVoo(Long vooId) {
        return convertToDTOList(reservaQueryRepository.findByVooId(vooId));
    }
}