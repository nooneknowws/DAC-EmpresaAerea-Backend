package br.ufpr.dac.reserva.dto;

public record CriarReservaDTO(
    Long clienteId,
    Long vooId,
    Long aeroportoOrigemId,
    Long aeroportoDestinoId,
    Double valor,
    Integer milhas
) {}