package br.ufpr.dac.voos.dto;

import java.math.BigDecimal;

import br.ufpr.dac.voos.models.Voo;

public class VooDTO {
    private long id;
    private String dataHoraPartida;
    private AeroportoDTO origem;
    private AeroportoDTO destino;
    private BigDecimal valorPassagem;
    private int quantidadeAssentos;
    private int quantidadePassageiros;
    private String status;

    public VooDTO(Voo voo) {
        this.id = voo.getId();
        this.dataHoraPartida = voo.getDataHoraPartida().toString();
        this.origem = new AeroportoDTO(voo.getOrigem());
        this.destino = new AeroportoDTO(voo.getDestino());
        this.valorPassagem = voo.getValorPassagem();
        this.quantidadeAssentos = voo.getQuantidadeAssentos();
        this.quantidadePassageiros = voo.getQuantidadePassageiros();
        this.status = voo.getStatus();
    }

    public long getId() {
        return id;
    }

    public String getDataHoraPartida() {
        return dataHoraPartida;
    }

    public AeroportoDTO getOrigem() {
        return origem;
    }

    public AeroportoDTO getDestino() {
        return destino;
    }

    public BigDecimal getValorPassagem() {
        return valorPassagem;
    }

    public int getQuantidadeAssentos() {
        return quantidadeAssentos;
    }

    public int getQuantidadePassageiros() {
        return quantidadePassageiros;
    }

    public String getStatus() {
        return status;
    }

}
