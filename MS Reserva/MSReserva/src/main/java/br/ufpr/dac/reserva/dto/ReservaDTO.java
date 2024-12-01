package br.ufpr.dac.reserva.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.ufpr.dac.reserva.model.Aeroporto;

public record ReservaDTO(
    Long id,
    LocalDateTime dataHora,
    Aeroporto origem,
    Aeroporto destino,
    Double valor,
    Integer milhas,
    String status,
    Long vooId,
    Long clienteId,
    List<HistoricoAlteracaoEstadoDTO> historicoAlteracaoEstado
) {}