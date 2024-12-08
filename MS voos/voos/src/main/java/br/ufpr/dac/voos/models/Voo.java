package br.ufpr.dac.voos.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "voos")
public class Voo {



	public Voo() {
		super();
	}



	public Voo(Long id, String codigoVoo, LocalDateTime dataHoraPartida, Aeroporto origem, Aeroporto destino,
			BigDecimal valorPassagem, int quantidadeAssentos, int quantidadePassageiros, String status) {
		super();
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



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo_voo")
    private String codigoVoo;
    
    @Column(name = "data_hora_partida")
    private LocalDateTime dataHoraPartida;

    @ManyToOne
    @JoinColumn(name = "aeroporto_origem_id")
    private Aeroporto origem;

    @ManyToOne
    @JoinColumn(name = "aeroporto_destino_id")
    private Aeroporto destino;

    @Column(name = "valor_passagem")
    private BigDecimal valorPassagem;

    @Column(name = "quantidade_assentos")
    private int quantidadeAssentos;

    @Column(name = "quantidade_passageiros")
    private int quantidadePassageiros;

    @Column(name = "status")
    private String status;


    @PrePersist
    private void prePersist() {
        if (this.status == null) {
            this.status = "CONFIRMADO";
        }
    }


    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



	public String getCodigoVoo() {
		return codigoVoo;
	}



	public void setCodigoVoo(String codigoVoo) {
		this.codigoVoo = codigoVoo;
	}

    
}
