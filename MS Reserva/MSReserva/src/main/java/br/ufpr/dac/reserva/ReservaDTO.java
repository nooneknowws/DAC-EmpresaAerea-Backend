package br.ufpr.dac.reserva;

import java.time.LocalDateTime;

public class ReservaDTO {
	private Long id;
	private LocalDateTime dataHora;
	private Aeroporto origem;
	private Aeroporto destino;
	private Double valor;
	private Integer milhas;
	private String status;
	private Long vooId;
	private Long clienteId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public Aeroporto getOrigem() {
		return origem;
	}

	public void setOrigem(Aeroporto origem) {
		this.origem = origem;
	}

	public Aeroporto getDestino() {
		return destino;
	}

	public void setDestino(Aeroporto destino) {
		this.destino = destino;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getMilhas() {
		return milhas;
	}

	public void setMilhas(Integer milhas) {
		this.milhas = milhas;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVooId() {
		return vooId;
	}

	public void setVooId(Long vooId) {
		this.vooId = vooId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

}
