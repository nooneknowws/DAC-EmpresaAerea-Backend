package br.ufpr.dac.reserva.cqrs.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.ufpr.dac.reserva.model.Aeroporto;

public record CriarReservaCommand(
	    LocalDateTime dataHora,
	    Aeroporto origem,
	    Aeroporto destino,
	    BigDecimal valor,
	    Integer milhas,
	    Long vooId,
	    Long clienteId
	) {}