package br.ufpr.dac.reserva;

import java.time.LocalDateTime;

public class HistoricoAlteracaoEstadoDTO {
    private Long id;
    private LocalDateTime dataHoraAlteracao;
    private String estadoOrigem;
    private String estadoDestino;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHoraAlteracao() { return dataHoraAlteracao; }
    public void setDataHoraAlteracao(LocalDateTime dataHoraAlteracao) { this.dataHoraAlteracao = dataHoraAlteracao; }

    public String getEstadoOrigem() { return estadoOrigem; }
    public void setEstadoOrigem(String estadoOrigem) { this.estadoOrigem = estadoOrigem; }

    public String getEstadoDestino() { return estadoDestino; }
    public void setEstadoDestino(String estadoDestino) { this.estadoDestino = estadoDestino; }
}
