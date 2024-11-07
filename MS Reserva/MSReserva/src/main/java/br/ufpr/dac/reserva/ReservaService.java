package br.ufpr.dac.reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setDataHora(reserva.getDataHora());
        dto.setOrigem(reserva.getOrigem());
        dto.setDestino(reserva.getDestino());
        dto.setValor(reserva.getValor());
        dto.setMilhas(reserva.getMilhas());
        dto.setStatus(reserva.getStatus().name());
        dto.setVooId(reserva.getVooId());
        dto.setClienteId(reserva.getClienteId());
        return dto;
    }

    public Reserva toEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setId(dto.getId());
        reserva.setDataHora(dto.getDataHora());
        reserva.setOrigem(dto.getOrigem());
        reserva.setDestino(dto.getDestino());
        reserva.setValor(dto.getValor());
        reserva.setMilhas(dto.getMilhas());
        reserva.setStatus(StatusReserva.valueOf(dto.getStatus()));
        reserva.setVooId(dto.getVooId());
        reserva.setClienteId(dto.getClienteId());
        return reserva;
    }

    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Reserva findById(Long id) throws Exception {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new Exception("Reserva não encontrada"));
    }
}
