package br.ufpr.dac.MSClientes.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "milhas")
public class Milhas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @Column(name = "data_hora_transacao", nullable = false)
    private LocalDateTime dataHoraTransacao;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "entrada_saida", nullable = false)
    private String entradaSaida;

    private String descricao;

    public Milhas() {}

    public Milhas(Usuario cliente, LocalDateTime dataHoraTransacao, Integer quantidade, String entradaSaida, String descricao) {
        this.cliente = cliente;
        this.dataHoraTransacao = dataHoraTransacao;
        this.quantidade = quantidade;
        this.entradaSaida = entradaSaida;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataHoraTransacao() {
        return dataHoraTransacao;
    }

    public void setDataHoraTransacao(LocalDateTime dataHoraTransacao) {
        this.dataHoraTransacao = dataHoraTransacao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getEntradaSaida() {
        return entradaSaida;
    }

    public void setEntradaSaida(String entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}