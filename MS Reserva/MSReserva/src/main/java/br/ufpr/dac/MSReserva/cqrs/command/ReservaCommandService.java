package br.ufpr.dac.MSReserva.cqrs.command;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufpr.dac.MSReserva.config.rabbitmq.RabbitMQConfig;
import br.ufpr.dac.MSReserva.cqrs.command.*;
import br.ufpr.dac.MSReserva.dto.CriarReservaDTO;
import br.ufpr.dac.MSReserva.events.ReservaEvent;
import br.ufpr.dac.MSReserva.model.Reserva;
import br.ufpr.dac.MSReserva.model.StatusReserva;
import jakarta.transaction.Transactional;

@Service
public class ReservaCommandService {

    @Autowired
    private ReservaCommandRepository reservaCommandRepository;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private void publishEvent(Reserva reserva, ReservaEvent.EventType tipo) {
        ReservaEvent event = new ReservaEvent();
        event.setTipo(tipo);
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

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME, 
            "reserva.sync.event", 
            event
        );

       
    }


    @Transactional
    public Reserva criarReserva(CriarReservaDTO dto) {
    	  System.out.println("=== Incoming DTO Values ===");
    	    System.out.println("codigoVoo: " + dto.codigoVoo());
    	    System.out.println("quantidade: " + dto.quantidade());

        Reserva reserva = new Reserva();
        reserva.setDataHora(LocalDateTime.now());
        reserva.setAeroportoOrigemCod(dto.aeroportoOrigemCod());
        reserva.setAeroportoDestinoCod(dto.aeroportoDestinoCod());
        reserva.setValor(dto.valor());
        reserva.setMilhas(dto.milhas());
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setVooId(dto.vooId());
        reserva.setClienteId(dto.clienteId());
        reserva.setCodigoReserva(gerarCodigoReserva());
        reserva.setCodigoVoo(dto.codigoVoo());      
        reserva.setQuantidade(dto.quantidade());     
        

        reserva = reservaCommandRepository.save(reserva);
        
        publishEvent(reserva, ReservaEvent.EventType.CREATED);
        return reserva;
    }

    @Transactional
    public Reserva atualizarStatusReserva(Long reservaId, StatusReserva novoStatus) {
        Reserva reserva = reservaCommandRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        reserva.adicionarHistoricoAlteracaoEstado(reserva.getStatus(), novoStatus);
        reserva.setStatus(novoStatus);
        reservaCommandRepository.save(reserva);
        return reserva;
    }

    private String gerarCodigoReserva() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 3).toUpperCase() + "-" + uuid.toString().substring(3, 6);
    }
}
