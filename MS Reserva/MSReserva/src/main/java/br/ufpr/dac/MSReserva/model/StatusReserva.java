package br.ufpr.dac.MSReserva.model;

public enum StatusReserva {
	PENDENTE("Pendente"), 
	FINALIZADO("Finalizada"), 
	CANCELADO("Cancelada"), 
	EMBARCADO("Embarcado"),
	CONFIRMADO("Confirmado");

	private final String descricao;

	StatusReserva(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}