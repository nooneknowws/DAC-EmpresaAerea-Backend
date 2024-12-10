package br.ufpr.dac.MSReserva.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ReservaDTO(
    Long id,
    LocalDateTime dataHora,
    String aeroportoOrigemCod,
    String aeroportoDestinoCod,
    Double valor,
    Integer milhas,
    String status,
    Long vooId,
    Long clienteId,
    String codigoReserva,
    String codigoVoo,
    Long quantidade,
    List<HistoricoAlteracaoEstadoDTO> historicoAlteracaoEstado
) {}
