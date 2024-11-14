package br.ufpr.dac.reserva.dto;

import br.ufpr.dac.reserva.model.Aeroporto;

public record CriarReservaDTO(
    Long clienteId,
    Long vooId,
    Aeroporto origem,
    Aeroporto destino,
    Double valor,
    Integer milhas
) {}