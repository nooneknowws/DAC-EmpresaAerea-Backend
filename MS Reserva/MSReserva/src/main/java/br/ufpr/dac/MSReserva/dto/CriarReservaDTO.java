package br.ufpr.dac.MSReserva.dto;

public record CriarReservaDTO(
	String codigoReserva,	
    Long clienteId,
    Long vooId,
    String aeroportoOrigemCod,
    String aeroportoDestinoCod,
    Double valor,
    Integer milhas,
    String codigoVoo,
    Long quantidade
) {}