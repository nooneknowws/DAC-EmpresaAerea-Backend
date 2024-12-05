package br.ufpr.dac.voos.dto;

import br.ufpr.dac.voos.models.Aeroporto;

public class AeroportoDTO {
    private String codigo;
    private String nome;

    public AeroportoDTO(Aeroporto aeroporto) {
        this.codigo = aeroporto.getCodigo();
        this.nome = aeroporto.getNome();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }
    
}
