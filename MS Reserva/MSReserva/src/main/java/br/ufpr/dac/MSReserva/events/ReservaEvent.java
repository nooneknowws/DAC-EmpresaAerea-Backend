package br.ufpr.dac.MSReserva.events;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

import br.ufpr.dac.MSReserva.model.Reserva;

@Data
public class ReservaEvent implements Serializable {
	private EventType tipo;
    private Long id;
    private LocalDateTime dataHora;
    private String aeroportoOrigemCod;
    private String aeroportoDestinoCod;
    private Double valor;
    private Integer milhas;
    private String status;
    private Long vooId;
    private Long clienteId;
    private String codigoVoo;    
    private Long quantidade;     
    private String codigoReserva;
    @Override
    public String toString() {
        return "ReservaEvent{" +
            "tipo=" + tipo +
            ", id=" + id +
            ", codigoVoo='" + codigoVoo + '\'' +
            ", quantidade=" + quantidade +
            ", status='" + status + '\'' +
            '}';
    }
    
    public enum EventType {
        CREATED, UPDATED, DELETED
    }
    public static ReservaEvent fromReserva(Reserva reserva) {
        
        ReservaEvent event = new ReservaEvent();
    	
        event.setId(reserva.getId());
        event.setDataHora(reserva.getDataHora());
        event.setAeroportoOrigemCod(reserva.getAeroportoOrigemCod());
        event.setAeroportoDestinoCod(reserva.getAeroportoDestinoCod());
        event.setValor(reserva.getValor());
        event.setMilhas(reserva.getMilhas());
        event.setStatus(reserva.getStatus().name());
        event.setVooId(reserva.getVooId());
        event.setClienteId(reserva.getClienteId());
        event.setCodigoReserva(reserva.getCodigoReserva());
        event.setCodigoVoo(reserva.getCodigoVoo());       
        event.setQuantidade(reserva.getQuantidade());  
        System.out.println("=== Created Event ===");
        System.out.println(event.toString());
        return event;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public String getAeroportoOrigemCod() {
		return aeroportoOrigemCod;
	}

	public void setAeroportoOrigemCod(String aeroportoOrigemCod) {
		this.aeroportoOrigemCod = aeroportoOrigemCod;
	}

	public String getAeroportoDestinoCod() {
		return aeroportoDestinoCod;
	}

	public void setAeroportoDestinoCod(String aeroportoDestinoCod) {
		this.aeroportoDestinoCod = aeroportoDestinoCod;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getMilhas() {
		return milhas;
	}

	public void setMilhas(Integer milhas) {
		this.milhas = milhas;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVooId() {
		return vooId;
	}

	public void setVooId(Long vooId) {
		this.vooId = vooId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public String getCodigoReserva() {
		return codigoReserva;
	}

	public void setCodigoReserva(String codigoReserva) {
		this.codigoReserva = codigoReserva;
	}

	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
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