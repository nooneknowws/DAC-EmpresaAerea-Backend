package br.ufpr.dac.reserva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Aeroporto {
    @Column(name = "aeroporto_id")
    private Long id;
    
    @Column(name = "aeroporto_codigo")
    private String codigo;
    
    @Column(name = "aeroporto_nome")
    private String nome;
    
    @Column(name = "aeroporto_cidade")
    private String cidade;
    
    @Column(name = "aeroporto_estado")
    private String estado;
    
    @Column(name = "aeroporto_pais")
    private String pais;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
}