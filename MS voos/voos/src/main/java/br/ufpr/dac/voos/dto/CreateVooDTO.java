package br.ufpr.dac.voos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateVooDTO {
    private LocalDateTime dataHoraPartida;
    private String codigoOrigem;
    private String codigoDestino;
    private BigDecimal valorPassagem;
    private int quantidadeAssentos;
    private int quantidadePassageiros;

    public CreateVooDTO() {
        this.dataHoraPartida = getDataHoraPartida();
        this.codigoOrigem = getCodigoOrigem();
        this.codigoDestino = getCodigoDestino();
        this.valorPassagem = getValorPassagem();
        this.quantidadeAssentos = getQuantidadeAssentos();
        this.quantidadePassageiros = getQuantidadePassageiros();
    }

    public LocalDateTime getDataHoraPartida() {
        return dataHoraPartida;
    }

    public void setDataHoraPartida(LocalDateTime dataHoraPartida) {
        this.dataHoraPartida = dataHoraPartida;
    }

    public String getCodigoOrigem() {
        return codigoOrigem;
    }

    public void setCodigoOrigem(String codigoOrigem) {
        this.codigoOrigem = codigoOrigem;
    }

    public String getCodigoDestino() {
        return codigoDestino;
    }

    public void setCodigoDestino(String codigoDestino) {
        this.codigoDestino = codigoDestino;
    }

    public BigDecimal getValorPassagem() {
        return valorPassagem;
    }

    public void setValorPassagem(BigDecimal valorPassagem) {
        this.valorPassagem = valorPassagem;
    }

    public int getQuantidadeAssentos() {
        return quantidadeAssentos;
    }

    public void setQuantidadeAssentos(int quantidadeAssentos) {
        this.quantidadeAssentos = quantidadeAssentos;
    }

    public int getQuantidadePassageiros() {
        return quantidadePassageiros;
    }

    public void setQuantidadePassageiros(int quantidadePassageiros) {
        this.quantidadePassageiros = quantidadePassageiros;
    }
}