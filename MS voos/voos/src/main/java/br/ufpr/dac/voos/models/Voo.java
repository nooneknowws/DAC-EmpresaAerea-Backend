package br.ufpr.dac.voos.models;

import java.time.LocalDateTime;


public class Voo {
    public String id;
    public String codigoVoo;
    public LocalDateTime dataHoraPartida;
    public Aeroporto origem;
    public Aeroporto destino; 
    public double valorPassagem;
    public int quantidadeAssentos;
    public int quantidadePassageiros;
    public String status;

    public Voo() {
        super();
    }

    public Voo(String id, String codigoVoo, LocalDateTime dataHoraPartida, Aeroporto origem, Aeroporto destino, double valorPassagem, int quantidadeAssentos, int quantidadePassageiros, String status) {
        this.id = id;
        this.codigoVoo = codigoVoo;
        this.dataHoraPartida = dataHoraPartida;
        this.origem = origem;
        this.destino = destino;
        this.valorPassagem = valorPassagem;
        this.quantidadeAssentos = quantidadeAssentos;
        this.quantidadePassageiros = quantidadePassageiros;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoVoo() {
        return codigoVoo;
    }

    public void setCodigoVoo(String codigoVoo) {
        this.codigoVoo = codigoVoo;
    }

    public LocalDateTime getDataHoraPartida() {
        return dataHoraPartida;
    }

    public void setDataHoraPartida(LocalDateTime dataHoraPartida) {
        this.dataHoraPartida = dataHoraPartida;
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

    public double getValorPassagem() {
        return valorPassagem;
    }

    public void setValorPassagem(double valorPassagem) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
