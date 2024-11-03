package main.java.com.voos.ms_voos;

public class VooModel {
    public String id;
    public String codigoVoo;
    public DateTime dataHoraPartida;
    public DateTime dataHoraChegada; // ?
    public String origem; // TODO: TROCAR PELA CLASSE DE AEROPORTO
    public String destino; // TODO: TROCAR PELA CLASSE DE AEROPORTO
    public double valorPassagem;
    public int quantidadeAssentos;
    public int quantidadePassageiros;
    public String status;

    public VooModel() {
        super();
    }

    public VooModel(String id, String codigoVoo, DateTime dataHoraPartida, DateTime dataHoraChegada, String origem, String destino, double valorPassagem, int quantidadeAssentos, int quantidadePassageiros, String status) {
        this.id = id;
        this.codigoVoo = codigoVoo;
        this.dataHoraPartida = dataHoraPartida;
        this.dataHoraChegada = dataHoraChegada;
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

    public DateTime getDataHoraPartida() {
        return dataHoraPartida;
    }

    public void setDataHoraPartida(DateTime dataHoraPartida) {
        this.dataHoraPartida = dataHoraPartida;
    }

    public DateTime getDataHoraChegada() {
        return dataHoraChegada;
    }

    public void setDataHoraChegada(DateTime dataHoraChegada) {
        this.dataHoraChegada = dataHoraChegada;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
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
