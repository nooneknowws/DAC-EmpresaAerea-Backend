package br.ufpr.dac.reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufpr.dac.reserva.dto.ReservaDTO;
import br.ufpr.dac.reserva.model.Reserva;
import br.ufpr.dac.reserva.model.StatusReserva;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Transactional
    public Reserva criarReserva(ReservaDTO reservaDTO) {
        Reserva reserva = new Reserva();
        reserva.setDataHora(LocalDateTime.now());
        reserva.setOrigem(reservaDTO.origem());
        reserva.setDestino(reservaDTO.destino());
        reserva.setValor(reservaDTO.valor());
        reserva.setMilhas(reservaDTO.milhas());
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setVooId(reservaDTO.vooId());
        reserva.setClienteId(reservaDTO.clienteId());

        String codigoReserva = gerarCodigoReserva();
        reserva.setCodigoReserva(codigoReserva);

        reservaRepository.save(reserva);
        return reserva;
    }

    public Optional<Reserva> consultarReserva(Long id) {
        return reservaRepository.findById(id);
    }

    @Transactional
    public Reserva atualizarStatusReserva(Long reservaId, StatusReserva novoStatus) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        reserva.adicionarHistoricoAlteracaoEstado(reserva.getStatus(), novoStatus);
        reserva.setStatus(novoStatus);
        reservaRepository.save(reserva);
        return reserva;
    }

    @Transactional
    public Reserva cancelarReserva(Long reservaId) {
        return atualizarStatusReserva(reservaId, StatusReserva.CANCELADO);
    }

    @Transactional
    public Reserva realizarCheckIn(Long reservaId) {
        return atualizarStatusReserva(reservaId, StatusReserva.EMBARCADO);
    }

    public List<Reserva> listarReservasPorCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId);
    }

    public List<Reserva> listarReservasPorVoo(Long vooId) {
        return reservaRepository.findByVooId(vooId);
    }
    
    private String gerarCodigoReserva() {
        UUID uuid = UUID.randomUUID();
        String codigo = uuid.toString().substring(0, 3).toUpperCase() + "-" + uuid.toString().substring(3, 6);
        return codigo;
    }
}