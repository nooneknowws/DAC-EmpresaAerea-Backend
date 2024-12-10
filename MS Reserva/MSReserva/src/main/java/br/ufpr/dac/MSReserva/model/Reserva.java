package br.ufpr.dac.MSReserva.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.dac.MSReserva.dto.HistoricoAlteracaoEstadoDTO;
import br.ufpr.dac.MSReserva.dto.ReservaDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "data_hora")
    private LocalDateTime dataHora;
    
    @Column(name = "aeroporto_origem_Cod")
    private String aeroportoOrigemCod;
    
    @Column(name = "aeroporto_destino_Cod")
    private String aeroportoDestinoCod;
    
    private String codigoVoo;

    private Double valor;
    private Integer milhas;
    private String codigoReserva;
    private Long quantidade;

    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    private Long vooId;
    private Long clienteId;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoAlteracaoEstado> historicoAlteracaoEstado = new ArrayList<>();
    
    public ReservaDTO toDTO() {
        List<HistoricoAlteracaoEstadoDTO> historicoAlteracaoEstadoDTO = historicoAlteracaoEstado.stream()
            .map(HistoricoAlteracaoEstado::toDTO)
            .toList();

        return new ReservaDTO(
            id,
            dataHora,
            aeroportoOrigemCod,
            aeroportoDestinoCod,
            valor,
            milhas,
            status.toString(),
            vooId,
            clienteId,
            codigoReserva,
            codigoVoo,
            quantidade,
            historicoAlteracaoEstadoDTO
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getAeroportoOrigemCod() { return aeroportoOrigemCod; }
    public void setAeroportoOrigemCod(String aeroportoOrigemCod) { this.aeroportoOrigemCod = aeroportoOrigemCod; }

    public String getAeroportoDestinoCod() { return aeroportoDestinoCod; }
    public void setAeroportoDestinoCod(String aeroportoDestinoCod) { this.aeroportoDestinoCod = aeroportoDestinoCod; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public Integer getMilhas() { return milhas; }
    public void setMilhas(Integer milhas) { this.milhas = milhas; }
    
    public String getCodigoReserva() { return codigoReserva; }
	public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }

    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }

    public Long getVooId() { return vooId; }
    public void setVooId(Long vooId) { this.vooId = vooId; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public List<HistoricoAlteracaoEstado> getHistoricoAlteracaoEstado() { return historicoAlteracaoEstado; }
    public void setHistoricoAlteracaoEstado(List<HistoricoAlteracaoEstado> historicoAlteracaoEstado) { 
        this.historicoAlteracaoEstado = historicoAlteracaoEstado; 
    }

    public void adicionarHistoricoAlteracaoEstado(StatusReserva estadoOrigem, StatusReserva estadoDestino) {
        HistoricoAlteracaoEstado historico = new HistoricoAlteracaoEstado(this, LocalDateTime.now(), estadoOrigem, estadoDestino);
        historicoAlteracaoEstado.add(historico);
    }

	public String getCodigoVoo() {
		return codigoVoo;
	}

	public void setCodigoVoo(String codigoVoo) {
		this.codigoVoo = codigoVoo;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
}
