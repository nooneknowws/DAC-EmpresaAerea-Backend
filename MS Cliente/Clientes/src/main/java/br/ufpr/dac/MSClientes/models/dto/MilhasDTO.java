package br.ufpr.dac.MSClientes.models.dto;

public class MilhasDTO {
    private Long clienteId;
    private Integer quantidade;
    private Long valorEmReais;
    private String descricao;
    private Long reservaId;
    private String entradaSaida;
    
    public Long getValorEmReais() {return valorEmReais;}
    public void setValorEmReais(Long valorEmReais) {this.valorEmReais = valorEmReais;}
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }
    public String getEntradaSaida() { return entradaSaida; }
    public void setEntradaSaida(String entradaSaida) { this.entradaSaida = entradaSaida; }
}
