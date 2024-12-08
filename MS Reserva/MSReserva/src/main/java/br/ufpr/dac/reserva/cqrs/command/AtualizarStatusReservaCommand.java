package br.ufpr.dac.reserva.cqrs.command;

import br.ufpr.dac.reserva.model.StatusReserva;

public record AtualizarStatusReservaCommand(
	    Long reservaId,
	    StatusReserva novoStatus
	) {}